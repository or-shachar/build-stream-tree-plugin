package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.graph.clone.Relinker;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.*;
import hudson.plugins.parameterizedtrigger.AbstractBuildParameters;
import hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig;
import hudson.plugins.parameterizedtrigger.BuildTrigger;
import hudson.plugins.parameterizedtrigger.BuildTriggerConfig;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import join.JoinTrigger;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/2/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 *
 * change target projects for Join Triggers after cloning to use the new cloned projects instead of their originals.
 */
public class JoinTriggerRelinker implements Relinker<AbstractProject> {

    @Override
    public void relink(Map<AbstractProject, AbstractProject> original2cloned) {

        final Collection<AbstractProject> outdated = original2cloned.values();

        for (Job job : outdated) {

            if (job instanceof AbstractProject) {

                AbstractProject project = (AbstractProject)job;
                relink(project, original2cloned);
            }
        }
    }

    /*
     * this function just delegates to relinking logic per each project type
     */
    private void relink(AbstractProject project, Map<AbstractProject, AbstractProject> original2cloned) {

        final Set<AbstractProject> originals = original2cloned.keySet();


        if (project instanceof Project) {
            Project p = ((Project) project);
            relinkList(original2cloned, originals, p.getPublishersList());
        }
        else if (project instanceof MatrixProject) {
            MatrixProject matrixProject = ((MatrixProject) project);
            relinkList(original2cloned, originals, matrixProject.getPublishersList());
        }
        else if (project instanceof MavenModuleSet) {
            MavenModuleSet mavenProject = (MavenModuleSet) project;
            relinkList(original2cloned, originals, mavenProject.getPublishersList());
        }

        //TODO unite all rebuildDependencyGraph invocations to a single call after all relinking is done.
        Jenkins.getInstance().rebuildDependencyGraph();
    }

    private void relinkList(
            Map<AbstractProject, AbstractProject> original2cloned,
            Set<AbstractProject> originals,
            DescribableList pl) {

        //if there's a join trigger, there's only one, the for iteration handles it if it exists or not.
        final List<JoinTrigger> joinTriggers = pl.getAll(JoinTrigger.class);
        for (JoinTrigger joinTrigger : joinTriggers) {

            final DescribableList<Publisher, Descriptor<Publisher>> joinPublishers = joinTrigger.getJoinPublishers();
            for (Publisher p : joinPublishers) {

                //join-trigger relies heavily on parameterized-build-triggers
                if (p instanceof BuildTrigger) {

                    //TODO: this entire code section is the same as the parameterized build trigger relinker.. unite!
                    final List<BuildTriggerConfig> configs = ((BuildTrigger) p).getConfigs();

                    if (configs != null) {

                        /**
                         * we'll iterate a copy and work on the original so we won't have to update the reference the
                         * proejct already keeps to the config list.
                         */

                        final List configsCopy = new ArrayList(configs);

                        configs.clear();

                        for (Object obbtc : configsCopy) {

                            BuildTriggerConfig bbtc = (BuildTriggerConfig)obbtc;

                            final List<AbstractProject> projectList = bbtc.getProjectList(null);

                            //if the triggered projects contain any of our cloned projects...
                            if (CollectionUtils.containsAny(projectList, originals)) {

                                //relink the project list
                                final List<AbstractBuildParameters> pConfigs = bbtc.getConfigs();
                                final List<AbstractProject> transferredProjectList =
                                    RelinkingUtils.transferList(original2cloned, projectList);
                                final String transferredProjects = Items.toNameList(transferredProjectList);

                                //create an updated config according to it's type
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

                            //for configurations we didn't modify - leave them as is
                            else {

                                configs.add(bbtc);
                            }
                        }
                    }
                }
            }

            //create a new relinked list from the JoinTrigger.getJoinProjects() list
            final List<AbstractProject> relinkedProjects =
                    RelinkingUtils.transferList(original2cloned, joinTrigger.getJoinProjects());

            String relinkedString = Items.toNameList(relinkedProjects);

            //remove the old jointrigger ...
            try {
                pl.remove(joinTrigger);
            } catch (IOException e) {
                //TODO; handle exceptions logging
            }

            //and insert a relinked (updated) one.
            try {
                pl.add(new JoinTrigger(
                        joinTrigger.getJoinPublishers(),
                        relinkedString,
                        joinTrigger.getEvenIfDownstreamUnstable()));
            } catch (IOException e) {
                //TODO; handle exceptions logging
            }

        }
    }
}
