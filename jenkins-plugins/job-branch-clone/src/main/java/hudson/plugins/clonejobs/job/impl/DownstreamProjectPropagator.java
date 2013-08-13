package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.model.AbstractProject;
import hudson.model.Job;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/24/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 *
 * returns all the downstream project for a given project, according to the jenkins graph model.
 *
 */
public class DownstreamProjectPropagator implements Propagator<Job> {


    @Override
    public Collection<Job> propagate(Job job) {

        if (job instanceof AbstractProject) {

            final AbstractProject abstractProjectJob = (AbstractProject) job;
            return abstractProjectJob.getDownstreamProjects();
        }

        return Collections.emptyList();
    }
}
