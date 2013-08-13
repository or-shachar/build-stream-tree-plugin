package hudson.plugins.clonejobs.view.impl;

import com.hp.commons.core.collection.factory.HashSetFactory;
import com.hp.commons.core.combiner.CombinerFactory;
import com.hp.commons.core.combiner.impl.CollectionCombinationBuilder;
import com.hp.commons.core.handler.VoidHandler;
import com.hp.commons.core.handler.collector.CollectingHandler;
import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;
import com.hp.commons.core.graph.impl.TraversalFactory;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.Traversal;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.clonejobs.job.domlogic.XmlBasedJobPropagator;
import hudson.plugins.clonejobs.job.impl.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/24/12
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 *
 * collects all propagated jobs from a group of selected jobs in a view recursively.
 *
 * TODO: aach, why iterate the view? you already have the names of the jobs! just iterate them and do whatever you want.
 */
public class ViewTraversalPropagatedJobsCollector implements VoidHandler<View> {

    private Map<Job, Collection<Job>> collectedPropagatedJobs;
    private Collection<String> selectedJobs;
    private Collection<Job> alreadyProcessed;
    private Traversal<Void, Void, Job> jobTraverser;

    /**
     *
     * @param propagatedJobs result container for propagated jobs found for the given selecetdJobs
     * @param selectedJobs whose propagations should be found in this view.
     */
    public ViewTraversalPropagatedJobsCollector(
            Map<Job, Collection<Job>> propagatedJobs,
            Collection<String> selectedJobs) {

        this.alreadyProcessed = new ArrayList<Job>();

        this.selectedJobs = selectedJobs;
        this.collectedPropagatedJobs = propagatedJobs;

        Propagator<Job> jobPropagator =
                new XmlBasedJobPropagator();
            /*CombinerFactory.combine(
                Propagator.class,
                //TODO: should be extensible...
                Arrays.<Propagator<Job>>asList(
                    new TriggeredProjectPropagator(),
                    new DownstreamProjectPropagator(),
                    new JoinTriggerPropagator(),
                    new CopyArtifactsPropagator(),
                    new ConditionalStepPropagator()
                ),
                new CollectionCombinationBuilder(new HashSetFactory()));
                */

        this.jobTraverser = new NonRepeatingTraversal<Void, Void, Job>(
                TraversalFactory.<Void, Job>getTraversal(null, jobPropagator));

    }

    @Override
    public Void apply(View node) {

        final Collection<TopLevelItem> items = node.getItems();

        for (TopLevelItem item : items) {

            if (item instanceof Job) {
                final Job job = (Job) item;

                handle(job);
            }
        }

        return null;
    }

    private void handle(Job job) {

        //only determine propagations of jobs the user selected to clone
        if (selectedJobs.contains(job.getFullDisplayName())) {

            Collection<Job> propagatedJobs = new LinkedHashSet<Job>();

            this.jobTraverser.setNodeHandler(new CollectingHandler<Job>(propagatedJobs));
            this.jobTraverser.apply(job);

            //remove duplicates when there are dependencies between the selected job.
            removeSelectedFromPropagated(propagatedJobs);

            //remove duplicates when several projects are dependant on the same project
            propagatedJobs.removeAll(alreadyProcessed);

            //TODO: seriously? this line isn't a bit redundant?
            propagatedJobs.addAll(propagatedJobs);

            alreadyProcessed.addAll(propagatedJobs);

            collectedPropagatedJobs.put(job, propagatedJobs);
        }
    }

    //TODO replace with CollectionUtils.filter
    private void removeSelectedFromPropagated(Collection<? extends Job> propagated) {

        final Iterator<? extends Job> iterator = propagated.iterator();
        while (iterator.hasNext()) {

            Job propagatedProject = iterator.next();

            if (selectedJobs.contains(propagatedProject.getFullDisplayName())) {
                iterator.remove();
            }
        }
    }
}
