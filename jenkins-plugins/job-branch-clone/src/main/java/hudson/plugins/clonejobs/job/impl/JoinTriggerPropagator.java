package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.parameterizedtrigger.BuildTrigger;
import hudson.plugins.parameterizedtrigger.BuildTriggerConfig;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import join.JoinTrigger;

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
 * propagator for JoinTrigger publisher. given a "source" project it will return the target projects of the JoinTrigger
 * publisher as defined in "source" if it is indeed defined in source.
 */
public class JoinTriggerPropagator implements Propagator<Job> {

    @Override
    //TODO: this should all be done in abstract propagator or something, only findTriggeringBuilders should be implemented...
    /**
     * handles the different project types and delegates the actual lgic to findTriggeringBuilders
     */
    public Collection<Job> propagate(Job source) {

        final ArrayList<Job> ret = new ArrayList<Job>();

        if (source instanceof Project) {

            final Project project = (Project) source;
            findTriggeringBuilders(ret, project.getBuildersList());
            findTriggeringBuilders(ret, project.getPublishersList());
        }

        if (source instanceof MavenModuleSet) {

            final MavenModuleSet project = (MavenModuleSet) source;
            findTriggeringBuilders(ret, project.getPrebuilders());
            findTriggeringBuilders(ret, project.getPostbuilders());
            findTriggeringBuilders(ret, project.getPublishersList());
        }

        if (source instanceof MatrixProject) {

            final MatrixProject project = (MatrixProject) source;
            findTriggeringBuilders(ret, project.getBuildersList());
            findTriggeringBuilders(ret, project.getPublishersList());
        }

        return ret;
    }

    /**
     *
     * @param ret the collection into which the targets of the join trigger should be inserted
     * @param buildSteps the collection of buildsteps/publishers that should be search for joinTrigger instances.
     *
     * finds join trigger instances in "buildsteps" and adds their targets to "ret".
     */
    private void findTriggeringBuilders(
            ArrayList<Job> ret,
            DescribableList buildSteps) {

        if (buildSteps != null) {

            for (Object publisher: buildSteps) {

                //TODO instanceof?
                if (JoinTrigger.class.isAssignableFrom(publisher.getClass())) {

                    final JoinTrigger joinTrigger = (JoinTrigger) publisher;
                    ret.addAll(joinTrigger.getJoinProjects());

                    final DescribableList<Publisher, Descriptor<Publisher>> joinPublishers = joinTrigger.getJoinPublishers();
                    for (Publisher p : joinPublishers) {
                        if (p instanceof BuildTrigger) {

                            final List<BuildTriggerConfig> configs = ((BuildTrigger) p).getConfigs();
                            for (BuildTriggerConfig config : configs) {
                                ret.addAll(config.getProjectList(null));
                            }
                        }
                    }
                }
            }
        }

    }

}
