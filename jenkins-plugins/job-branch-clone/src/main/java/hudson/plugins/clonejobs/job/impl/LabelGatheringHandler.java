package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.handler.Handler;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Label;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/6/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 *
 * gathers the labels of visited projects into a collection
 * TODO changing handler and gathering handler should share a superclass with common functionality or something
 */
public class LabelGatheringHandler implements Handler<Void, Job> {

    /**
     * constant used instead of "no restriction" (e.g. null). displayed to the user in UI.
     * TODO this should be in UI somewhere, probably in CloneJobsAction, and the map should be made agnostic to it,
     * by replacing null and "--empty--" symbols... approriately
     */
    public static final String UNDEFINED_LABEL_BINDING = "--empty--";

    /**
     * the collection into which label names of visited projects should be gathered
     */
    private Set<String> labelNamesContainer;

    /**
     *
     * @param labelNamesContainer the collection into which label names of visited projects should be gathered
     */
    public LabelGatheringHandler(Set<String> labelNamesContainer) {

        this.labelNamesContainer = labelNamesContainer;
    }

    @Override
    public Void apply(Job job) {

        //matrix defined labels are different than regular projects
        if (job instanceof MatrixProject) {
            apply((MatrixProject)job);
        }

        //TODO matrix projects can be run on different nodes nowadays, so remove the "else"
        else if (job instanceof AbstractProject) {
            apply((AbstractProject) job);
        }

        return null;
    }

    public void apply(MatrixProject matrix) {

        final Set<Label> labels = matrix.getLabels();

        //TODO addAll(labels)?
        for (Label label : labels) {
            this.labelNamesContainer.add(label.getName());
        }
    }

    public void apply(AbstractProject project) {

        String currentLabel = project.getAssignedLabelString();
        if (currentLabel == null) {
            currentLabel = UNDEFINED_LABEL_BINDING;
        }

        this.labelNamesContainer.add(currentLabel);
    }
}
