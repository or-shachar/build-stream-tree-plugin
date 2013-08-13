package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.graph.clone.Relinker;
import hudson.adapters.PersistedListWrapper;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import hudson.model.Items;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.parameterizedtrigger.*;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 *
 * relinker for parameterized-trigger build step. will replace the names of the projects to trigger according to the
 * original->cloned mapping.
 */
public class TriggeredJobsRelinker implements Relinker<Job> {

    @Override
    public void relink(Map<Job, Job> original2cloned) {

        final Collection<Job> outdated = original2cloned.values();

        for (Job job : outdated) {

            if (job instanceof AbstractProject) {

                AbstractProject project = (AbstractProject)job;
                relink(project, original2cloned);
            }
        }
    }

    /**
     *
     * @param project whose parameterizd-trigger buildsteps need updating
     * @param original2cloned mapping to be used for updating
     *
     * this function just handles the different project types and delegates to relinkList
     * TODO as usual - this should be done in some abstract superclass
     */
    private void relink(AbstractProject project, Map<Job, Job> original2cloned) {

        if (project instanceof Project) {
            Project p = ((Project) project);
            relinkList(original2cloned, p.getBuildersList());
            relinkList(original2cloned, p.getPublishersList());
        }
        else if (project instanceof MatrixProject) {
            MatrixProject matrixProject = ((MatrixProject) project);
            relinkList(original2cloned, matrixProject.getBuildersList());
            relinkList(original2cloned, matrixProject.getPublishersList());
        }
        else if (project instanceof MavenModuleSet) {
            MavenModuleSet mavenProject = (MavenModuleSet) project;
            relinkList(original2cloned, mavenProject.getPrebuilders());
            relinkList(original2cloned, mavenProject.getPostbuilders());
            relinkList(original2cloned, mavenProject.getPublishersList());
        }

        //TODO: this shouldn't be done here, but after all relinkers in JobsCloner
        Jenkins.getInstance().rebuildDependencyGraph();
    }

    /**
     *
     * @param original2cloned the mapping to use for the update
     * @param publishersList the list of buildsteps in which to search for and update parametrized-triggers
     *
     * comfort function, allows working with DescribaleList which is not a common interface like List.
     */
    private void relinkList(Map<Job, Job> original2cloned, DescribableList publishersList) {
        relinkList(original2cloned, new PersistedListWrapper(publishersList));
    }


    /**
     *
     * @param original2cloned the mapping to use for the update
     * @param pl the list of buildsteps in which to search for and update parametrized-triggers
     *
     * relinks parameterized-trigger buildsteps in pl according to the original2cloned map.
     *
     */
    public static void relinkList(Map<Job, Job> original2cloned, List pl) {


        for (Object builder: pl) {

            relinkBuilder(original2cloned, builder);
        }
    }

    /**
     *
     * @param original2cloned the mapping with which to relink the builder
     * @param builder buildstep to relink if necessary
     */
    public static void relinkBuilder(Map<Job, Job> original2cloned, Object builder) {

        final Set<Job> originals = original2cloned.keySet();
        List configs = null;

        //TODO instanceof?
        if (BuildTrigger.class.isAssignableFrom(builder.getClass())) {

            configs = ((BuildTrigger) builder).getConfigs();
        }
        else if (TriggerBuilder.class.isAssignableFrom(builder.getClass())) {
            configs = ((TriggerBuilder) builder).getConfigs();
        }

        if (configs != null) {

            //iterate a copy of the configs and clear the current config element because it's already refernced by
            //the project buildstep list.
            final List configsCopy = new ArrayList(configs);

            configs.clear();

            for (Object obbtc : configsCopy) {

                BuildTriggerConfig bbtc = (BuildTriggerConfig)obbtc;

                final List<AbstractProject> projectList = bbtc.getProjectList(null);

                //if the triggered projects contains any of our cloned projects...
                if (CollectionUtils.containsAny(projectList, originals)) {

                    //relink the configs
                    final List<AbstractBuildParameters> pConfigs = bbtc.getConfigs();
                    final List<AbstractProject> transferredProjectList =
                        RelinkingUtils.transferList(original2cloned, projectList);
                    final String transferredProjects = Items.toNameList(transferredProjectList);

                    //and create appropriate config element
                    final BuildTriggerConfig newConfig =
                        (bbtc instanceof BlockableBuildTriggerConfig) ?
                            new BlockableBuildTriggerConfig(
                                transferredProjects,
                                ((BlockableBuildTriggerConfig)bbtc).getBlock(),
                                    pConfigs) :
                            new BuildTriggerConfig(
                                    transferredProjects,
                                    ((BuildTriggerConfig)bbtc).getCondition(),
                                    ((BuildTriggerConfig)bbtc).getTriggerWithNoParameters(),
                                    pConfigs);

                    configs.add(newConfig);
                }

                //for configurations we didn't modify
                else {

                    configs.add(bbtc);
                }
            }
        }
    }


}