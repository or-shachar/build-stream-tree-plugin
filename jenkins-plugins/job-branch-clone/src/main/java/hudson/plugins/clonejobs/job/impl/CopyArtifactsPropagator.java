package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.copyartifact.CopyArtifact;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/2/12
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 *
 * TODO: put the different project type handling logic in some abstract class or something
 */
public class CopyArtifactsPropagator implements Propagator<Job> {

    @Override
    //delegates the logic to findTriggeringPropagation method with the appropriate buildsteps lists.
    public Collection<Job> propagate(Job source) {

        final ArrayList<Job> ret = new ArrayList<Job>();

        if (source instanceof Project) {

            final Project project = (Project) source;
            findTriggeringPropagation(ret, project.getBuildersList());
            findTriggeringPropagation(ret, project.getPublishersList());
        }

        if (source instanceof MavenModuleSet) {

            final MavenModuleSet project = (MavenModuleSet) source;
            findTriggeringPropagation(ret, project.getPrebuilders());
            findTriggeringPropagation(ret, project.getPostbuilders());
            findTriggeringPropagation(ret, project.getPublishersList());
        }

        if (source instanceof MatrixProject) {

            final MatrixProject project = (MatrixProject) source;
            findTriggeringPropagation(ret, project.getBuildersList());
            findTriggeringPropagation(ret, project.getPublishersList());
        }

        return ret;
    }

    /**
     *
     * @param ret a collection to which the propagated jobs should be added.
     * @param buildSteps the buildsteps that should be checked
     *
     * this method applies getPropagations on each of the CopyArtifact elements in the buildSteps list.
     */
    public static void findTriggeringPropagation(
            Collection<Job> ret,
            DescribableList buildSteps) {

        if (buildSteps != null) {

            final List<CopyArtifact> builders = buildSteps.getAll(CopyArtifact.class);
            for (CopyArtifact copyArtifactBuilder : builders) {
                    getPropagations(ret, copyArtifactBuilder);
            }
        }
    }

    /**
     *
     * @param ret a collection to which we add the propagted jobs from the copyArtifactBuilder object.
     * @param copyArtifactBuilder potentially a CopyArtifact buildstep from which to locate dependencies.
     */
    public static void getPropagations(Collection<Job> ret, Object copyArtifactBuilder) {

        if (copyArtifactBuilder instanceof CopyArtifact) {

            final String projectName = ((CopyArtifact) copyArtifactBuilder).getProjectName();
            final Job item = (Job) Jenkins.getInstance().getItem(projectName);

            if (item == null) {
                //TODO: add logging
            }

            else {
                ret.add(item);
            }
        }
    }

}
