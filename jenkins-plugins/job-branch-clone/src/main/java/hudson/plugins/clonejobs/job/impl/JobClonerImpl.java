package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.clone.Cloner;
import hudson.model.AbstractProject;
import hudson.model.Job;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO: rename to SingleJobCloner
 */
public class JobClonerImpl implements Cloner<Job> {


    private Map<String, String> jobTargets;

    public JobClonerImpl(Map<String, String> jobTargets) {
        this.jobTargets = jobTargets;
    }


    @Override
    public Job clone(Job job) {

        try {

            final AbstractProject copy =
                    Jenkins.getInstance().copy((AbstractProject) job, jobTargets.get(job.getFullDisplayName()));

            return copy;
        }

        catch (IOException e) {
            throw new RuntimeException("failed to clone job " + job.getFullDisplayName(), e);
        }
    }

}
