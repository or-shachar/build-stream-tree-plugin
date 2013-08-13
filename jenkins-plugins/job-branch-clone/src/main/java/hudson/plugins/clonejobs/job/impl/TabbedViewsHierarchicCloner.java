package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.handler.impl.NoOperationHandler;
import com.hp.commons.core.graph.impl.traversal.MultipleSourcesTraversalImpl;
import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;
import com.hp.commons.core.graph.impl.TraversalFactory;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.Traversal;
import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;
import hudson.model.Job;
import hudson.model.View;
import hudson.plugins.clonejobs.view.ClonedProjectsViewGenerator;
import hudson.plugins.clonejobs.view.impl.ViewCopyingCombiner;

import java.util.Map;

/**
 * copies the view-hierarchy branch for views that recursively contain one or more of the cloned jobs.
 */
public class TabbedViewsHierarchicCloner implements ClonedProjectsViewGenerator {

    /**
     * a propagator that leads from a top-level view all the way to the subviews containing a project that we cloned.
     */
    private Propagator<View> projectContainingSubgraphPropagator;

    /**
     * mapping of currently existing top-level views and new names for the clones.
     */
    private Map<View, String> sourceViewsTargetNames;

    /**
     *
     * @param sourceViewsTargetNames mapping of currently existing top-level views and new names for the clones.
     * @param projectContainingSubgraphPropagator a propagator that leads from a top-level view all the way to
     *                                            the subviews containing a project that we cloned.
     */
    public TabbedViewsHierarchicCloner(
            Map<View, String> sourceViewsTargetNames,
            Propagator<View> projectContainingSubgraphPropagator) {

        this.sourceViewsTargetNames = sourceViewsTargetNames;
        this.projectContainingSubgraphPropagator = projectContainingSubgraphPropagator;
    }

    @Override
    public void cloneViews(Map<Job, Job> original2Cloned) {

        //combiner that copies nested views
        PreOrderTraversalResultsCombiner<View, View, View> preOrderCombiner =
                new ViewCopyingCombiner(sourceViewsTargetNames, original2Cloned);

        //traversal that will traverse all views that lead to a project, and clone the views on the way back.
        final Traversal<View,View,View> cloningTraversal =
                TraversalFactory.<View, View, View>getPreOrderTraversal(
                    NoOperationHandler.<View>getInstance(),
                    projectContainingSubgraphPropagator,
                    preOrderCombiner);

        //we have a try catch because if this dies in the middle we'd still like the clonejobs process to end nicely.
        try {
            new MultipleSourcesTraversalImpl(
                    new NonRepeatingTraversal(cloningTraversal)).traverse(sourceViewsTargetNames.keySet());
        }
        catch (Exception e) {
            //TODO: add logging here
        }
    }
}