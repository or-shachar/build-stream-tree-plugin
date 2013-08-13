package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import hudson.matrix.Axis;
import hudson.matrix.AxisList;
import hudson.matrix.LabelAxis;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Label;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/6/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 *
 * given a job an instance of this class will update its label according to the map by changing
 * any currently configured label to it's value in the map
 */
public class LabelChangingHandler implements Handler<Void, Job> {

    /**
     * the label mapping to use when updating labels
     */
    private Map<String, String> labelMapping;

    /**
     *
     * @param labelMapping the label mapping to use when updating labels
     */
    public LabelChangingHandler(Map<String, String> labelMapping) {

        this.labelMapping = labelMapping;
    }

    @Override
    public Void apply(Job job) {

        //labels in matrix type projects are defined differently than in regular projects
        //note that matrixproject extends abstractproject - so the order of the conditionals is important.
        if (job instanceof MatrixProject) {
            apply((MatrixProject)job);
        }

        //TODO these days the matrix can also be executed on a node, so drop the else?
        else if (job instanceof AbstractProject) {
            apply((AbstractProject) job);
        }

        return null;
    }

    /**
     *
     * @param matrix the matrix project where the label changes should occur
     *
     * handles relinking for matrix type projects.
     */
    public void apply(MatrixProject matrix) {

        //TODO: this function would be better if it modified current data only if required - for
        //performance reason as persistance is expensive. currently always causes changes to project.

        //get all configured label axi - there can be several
        final AxisList axes = matrix.getAxes();
        final AxisList labelAxisList = axes.subList(LabelAxis.class);
        final Iterator<Axis> iterator = labelAxisList.iterator();

        //create a "result" container for the axi with the updated labels
        Collection<Axis> fixedLabelAxes = new ArrayList<Axis>();

        //TODO remove needFix var?
        boolean needFix = false;

        //foreach label axis
        while (iterator.hasNext()) {

            Axis labelAxis = iterator.next();

            //if it contains an outdated label
            final List<String> labels = labelAxis.getValues();
            if (CollectionUtils.containsAny(labels, this.labelMapping.keySet())) {

                needFix = true;

                //remove the axis from the project
                //TODO redundant? we remove them all at the end of this function...
                iterator.remove();

                //create a fixedLabels collection with replaced labels
                //TODO use RelinkingUtils transferElements and CollectionUtils.filter instead
                HashSet<String> fixedLabels = new HashSet<String>(labels.size());
                for (String label : labels) {

                    final String newValue =
                            this.labelMapping.containsKey(label) ?
                                    this.labelMapping.get(label) :
                                    label;

                    if (!newValue.equalsIgnoreCase(LabelGatheringHandler.UNDEFINED_LABEL_BINDING)) {
                        fixedLabels.add(newValue);
                    }
                }

                //fixedLabeis is empty only when configured target labels are empty in which case
                // the axis should be dropped. otherwise - we create a new axis with the updated labels.
                if (!fixedLabels.isEmpty()) {
                    //TODO what happens if fixedLabels is somehow illegal, i.e. containing a string that has no label?
                    fixedLabelAxes.add(new LabelAxis(labelAxis.getName(), new ArrayList<String>(fixedLabels)));
                }
            }

            //TODO: bug, what happens if no outdated labels exist in the matrix project?
            //fixed label axes should get the unmodified original axis added anyway.
            //this bug doesn't actually ever happen with current implementation....
        }

        //remove all previous configurations
        axes.removeAll(axes.subList(LabelAxis.class));

        //and insert newly updated ones
        axes.addAll(fixedLabelAxes);

        try {
            matrix.setAxes(axes);
        }

        catch (IOException e) {
            throw new RuntimeException("failed to update the axis list of matrix project '" +
                    matrix.getFullDisplayName() + "' to use new labels", e);
        }
    }

    /**
     *
     * @param project abstract project whose label may need updating
     *
     * handles updating the "restrict this job to node" according to the mapping.
     */
    public void apply(AbstractProject project) {

        String currentLabel = project.getAssignedLabelString();

        //our mapping doesn't know a null, if there was no previous value it gets the constant
        //so we must update the value that will be used as key to what the mapping expects.
        if (currentLabel == null) {
            currentLabel = LabelGatheringHandler.UNDEFINED_LABEL_BINDING;
        }

        //if we need to update
        if (this.labelMapping.containsKey(currentLabel)) {

            final String userSelectedLabelString = this.labelMapping.get(currentLabel);

            //same as above, mapping doesn't know about null values
            //TODO: maybe mapping should be updated with nulls where appropriate...
            Label label =
                userSelectedLabelString.equalsIgnoreCase(
                        LabelGatheringHandler.UNDEFINED_LABEL_BINDING) ?
                            null :
                            Jenkins.getInstance().getLabel(userSelectedLabelString);

            try {

                //TODO only do this if new label != old label (persistance operation expensive..)
                project.setAssignedLabel(label);
            }

            catch (IOException e) {
                throw new RuntimeException("failed to change project to label '" + userSelectedLabelString + "'",e);
            }
        }
    }
}
