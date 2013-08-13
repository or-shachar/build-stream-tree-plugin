package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.combiner.CombinerFactory;
import com.hp.commons.core.combiner.impl.NullCombinationBuilder;
import com.hp.commons.core.graph.clone.Cloner;
import com.hp.commons.core.graph.clone.Relinker;
import com.hp.commons.core.handler.Handler;
import hudson.model.Job;
import hudson.plugins.clonejobs.job.JobsCloner;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/28/12
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 *
 * class that clones the jobs
 */
public class ApiBasedJobsCloner implements JobsCloner {

    /**
     * relinker to use after each job has been cloned.
     */
    private Relinker<Job> relinker;

    /**
     * some kind of job modifier to use after cloning of each job.
     */
    private Handler<?, Job> postCloningProcessor;

    //TODO accept relinkers in constructor, have comfort constructor with this default. possibly relinkers should be extensible?

    /**
     *
     * @param postProcessor to run on each job once it's been cloned, before it's been relinked.
     */
    public ApiBasedJobsCloner(Handler<?, Job> postProcessor) {

        this.relinker = CombinerFactory.combine(
                Relinker.class,
                //TODO this should be extensible
                Arrays.asList(
                    new TriggeredJobsRelinker(),
                    new DownstreamProjectsRelinker(),
                    new CopyArtifactsRelinker(),
                    new JoinTriggerRelinker(),
                    new ConditionalStepRelinker() //TODO: this logic should be built in in each relinker..
                ),
                NullCombinationBuilder.getInstance());

        this.postCloningProcessor = postProcessor;
    }


    @Override
    public Map<Job, Job> clone(Map<String, String> jobTargets) {

        Cloner<Job> jobCloner = new JobClonerImpl(jobTargets);

        Map<Job, Job> original2cloned = new HashMap<Job,Job>();

        for (String jobName : jobTargets.keySet()) {

            Job job = Jenkins.getInstance().getItemByFullName(jobName, Job.class);
            final Job clonedJob = jobCloner.clone(job);

            postCloningProcessor.apply(clonedJob);

            original2cloned.put(job, clonedJob);
        }

        relinker.relink(original2cloned);

        //persist the newly cloned jobs after they have been modified
        final Collection<Job> clonedJobs = original2cloned.values();
        for (Job job : clonedJobs) {
            try {
                job.save();
            } catch (IOException e) {

                throw new RuntimeException("failed to persist job " + job.getFullDisplayName());
            }
        }

        return original2cloned;
    }

}
