package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.criteria.InstanceOfCriteria;
import com.hp.commons.core.graph.propagator.Propagator;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.model.Job;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/6/12
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 *
 * TODO all of these propagators should be in jenkins-commons module, they may be used by other plugins...
 */
public class MatrixInnerJobsPropagator implements Propagator<Job> {

    private MatrixInnerJobsPropagator() {}
    private static MatrixInnerJobsPropagator instance;
    public static MatrixInnerJobsPropagator getInstance() {
        if (instance == null) {
            instance = new MatrixInnerJobsPropagator();
        }
        return instance;
    }


    @Override
    public Collection<Job> propagate(Job source) {

        if (source instanceof MatrixProject) {

            MatrixProject matrix = ((MatrixProject) source);
            final Collection<Job> allJobs = (Collection<Job>)matrix.getAllJobs();
            CollectionUtils.filter(allJobs, new InstanceOfCriteria(MatrixConfiguration.class));

            return allJobs;
        }

        return Collections.emptyList();
    }
}
