package hudson.plugins.clonejobs.view.impl;

import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;
import hudson.model.*;
import hudson.plugins.nested_view.NestedView;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/30/12
 * Time: 12:13 AM
 * To change this template use File | Settings | File Templates.
 *
 * the problem is that owner must be specified at creation time (at least for ListView) while nested views are added
 * afterwards.
 * in order to hierarchically copy views you must have this back-forth duality.
 * that's what this combiner does.
 * given a view it creates a copy, add it to its parents subviews, then continue iterating using the
 * new copy as owner for the next iterations.
 */

//TODO: this class probably requires refactoring
public class ViewCopyingCombiner implements PreOrderTraversalResultsCombiner<View, View, View> {

    /**
     * mapping from original jobs to cloned counterparts. used to generate the list views that contain
     * the cloned jobs (leaf nodes in views tree)
     */
    private Map<Job, Job> original2cloned;

    /**
     * mapping from existing views to new cloned views names, used to know the names of the copies to make
     */
    private Map<View, String> sourceViewsTargetNames;

    /**
     *
     * @param sourceViewsTargetNames mapping from existing views to new cloned views names,
     *                               used to know the names of the copies to make
     * @param original2cloned mapping from original jobs to cloned counterparts.
     */
    public ViewCopyingCombiner(
            Map<View, String> sourceViewsTargetNames,
            Map<Job, Job> original2cloned) {

        this.sourceViewsTargetNames = sourceViewsTargetNames;
        this.original2cloned = original2cloned;
    }

    @Override
    public View combine(View node, View currentResult, List<Map.Entry<View, View>> traversalResults) {

        //determine parent container (jenkins singleton or nested view instance)
        //TODO change default to Jenkins.getInstance() instead of null and remove that test from clone functions
        NestedView parent =
                traversalResults.isEmpty() ?
                        null :
                        (NestedView)(traversalResults.get(traversalResults.size() - 1).getValue());

        View clonedView = null;

        if (node instanceof ListView) {

            clonedView = clone((ListView) node, parent);
        }

        else if (node instanceof NestedView) {

            clonedView = clone((NestedView) node, parent);
        }

        return clonedView;

    }

/* ------------------------------------------------------------------------------------
* replicate LIST view
* ------------------------------------------------------------------------------------*/

    private View clone(ListView listView, NestedView parent) {

        ViewGroup container = parent == null ? Jenkins.getInstance() : parent ;

        ListView clonedView = getOrCreateCloningListView(listView, container);
        addViewIfNecessary(container, clonedView);

        return copyListViewAttributes(clonedView, listView);
    }

    private View copyListViewAttributes(ListView clonedView, ListView listView) {

        final Collection<TopLevelItem> viewJobs = listView.getItems();

        for (TopLevelItem job : viewJobs) {

            try {
                if (original2cloned.containsKey(job)) {
                    clonedView.add((TopLevelItem)original2cloned.get(job));
                }
            }

            catch (IOException e) {

                //TODO better to log than to die...
                throw new RuntimeException("could not add job '" + job.getFullDisplayName() + "' from view '" +
                    listView.getViewName() + "' to '" + clonedView.getViewName() +
                    "', during ListView cloning." ,e);
            }
        }

        //TODO: remove redundant, already called in .add() function
        try {
            clonedView.save();
        }

        catch (IOException e) {

            //TODO better to log than to die...
            throw new RuntimeException(
                    "could not save view '" + clonedView.getViewName() + "', duplicate of '" +
                    listView.getViewName() + "'",e);
        }

        return clonedView;
    }

    private ListView getOrCreateCloningListView(ListView listView, ViewGroup container) {

        String cloneName = sourceViewsTargetNames.containsKey(listView) ?
                sourceViewsTargetNames.get(listView) :
                listView.getViewName();

        ListView clonedView = null;
        while (clonedView == null) {

            View preExistingView = container.getView(cloneName);
            if (preExistingView == null) {
                clonedView = new ListView(cloneName, container);
            }
            else {
                if ((preExistingView instanceof ListView)) {
                    clonedView = (ListView)preExistingView;
                }
                else {
                    cloneName += "1";
                }
            }
        }

        return clonedView;
    }

/* ------------------------------------------------------------------------------------
* replicate NESTED view
* ------------------------------------------------------------------------------------*/

    private View clone(NestedView nestedView, NestedView parent) {


        ViewGroup container = parent == null ? Jenkins.getInstance() : parent;

        NestedView clonedView = getOrCreateCloningNestedView(nestedView, container);
        addViewIfNecessary(container, clonedView);

        return clonedView;
    }

    private NestedView getOrCreateCloningNestedView(NestedView nestedView, ViewGroup container) {

        String cloneName = sourceViewsTargetNames.containsKey(nestedView) ?
                sourceViewsTargetNames.get(nestedView) :
                nestedView.getViewName();

        NestedView clonedView = null;
        while (clonedView == null) {

            View preExistingView = container.getView(cloneName);
            if (preExistingView == null) {
                clonedView = new NestedView(cloneName);
                NestedViewReflectionWorkarounds.setOwner(clonedView, container);
            }
            else {
                if ((preExistingView instanceof NestedView)) {
                    clonedView = (NestedView)preExistingView;
                }
                else {
                    cloneName += "1";
                }
            }
        }

        return clonedView;
    }

    private void addViewIfNecessary(ViewGroup container, View clonedView) {

        try {

            if (container.getView(clonedView.getViewName()) == null) {

                if (container == Jenkins.getInstance()) {
                    Jenkins.getInstance().addView(clonedView);
                }
                else if (container instanceof NestedView) {
                    NestedViewReflectionWorkarounds.AddView((NestedView)container, clonedView);
                }
            }
        }
        catch (IOException e) {
            //TODO better to log than to die...
            throw new RuntimeException("failed to add view '" + clonedView.getViewName() +
                    "' to '" + container.getDisplayName() + "'.");
        }
    }
}
