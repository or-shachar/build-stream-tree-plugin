package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.parameterizedtrigger.BuildTrigger;
import hudson.plugins.parameterizedtrigger.BuildTriggerConfig;
import hudson.plugins.parameterizedtrigger.TriggerBuilder;
import hudson.util.DescribableList;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/24/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 *
 * propagator for parameterized-trigger buildsteps.
 * given a project that has a paremeterized-trigger buildstep, will return the list of jobs triggered by the build-step
 * aggregating multiple such buildsteps.
 *
 * TODO relinker and propagator should have common logic
 */
public class TriggeredProjectPropagator implements Propagator<Job> {

    @Override
    /**
     * handle different job types
     * TODO as always, should be done in some abstract super class for all job-buildstep based propagators
     */
    public Collection<Job> propagate(Job job) {

        Collection <Job> ret = new LinkedHashSet<Job>();

        DescribableList publishersList = null;

        if (job instanceof Project) {

            Project project = (Project)job;
            findTriggeringPropagation(ret, project.getPublishersList());
            findTriggeringPropagation(ret, project.getBuildersList());
        }

        if (job instanceof MatrixProject) {

            MatrixProject project = (MatrixProject)job;
            findTriggeringPropagation(ret, project.getPublishersList());
            findTriggeringPropagation(ret, project.getBuildersList());
        }

        if (job instanceof MavenModuleSet) {

            MavenModuleSet project = (MavenModuleSet)job;
            findTriggeringPropagation(ret, project.getPublishersList());
            findTriggeringPropagation(ret, project.getPrebuilders());
            findTriggeringPropagation(ret, project.getPostbuilders());
        }

        //TIDI wtf? that's always null...
        findTriggeringPropagation(ret, publishersList);

        return ret;
    }

    public static void findTriggeringPropagation(Collection<Job> ret, DescribableList buildSteps) {

        if (buildSteps != null) {

            for (Object publisher: buildSteps) {

                getPropagations(ret, publisher);
            }
        }
    }

    public static void getPropagations(Collection<Job> ret, Object publisher) {

        //initialize the configs element appropriately
        List configs = null;

        //TODO instanceof?
        if (BuildTrigger.class.isAssignableFrom(publisher.getClass())) {

            configs = ((BuildTrigger) publisher).getConfigs();
        }

        //TODO instanceof?
        if (TriggerBuilder.class.isAssignableFrom(publisher.getClass())) {

            configs = ((TriggerBuilder) publisher).getConfigs();
        }

        if (configs != null) {

            for (Object obbtc : configs) {

                BuildTriggerConfig bbtc = (BuildTriggerConfig)obbtc;

                final List<AbstractProject> projectList = bbtc.getProjectList(null);
                ret.addAll(projectList);
            }
        }
    }
}
