package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.clone.Relinker;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Project;
import hudson.plugins.copyartifact.CopyArtifact;
import hudson.plugins.parameterizedtrigger.TriggerBuilder;
import hudson.tasks.BuildStep;
import hudson.tasks.Builder;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.conditionalbuildstep.ConditionalBuilder;
import org.jenkinsci.plugins.conditionalbuildstep.singlestep.SingleConditionalBuilder;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/2/12
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 *
 * activates relinking logic on buildsteps contained within condition steps
 *
 * TODO: create abstract type for all job-buildstep-relinkers that encapsulates the different project type handling
 * and the conditional buildsteps. only relink list should be implemented each time.
 *
 * TODO: relinkers should somehow use the associated propagators?
 */
public class ConditionalStepRelinker implements Relinker<Job> {

    @Override
    public void relink(Map<Job, Job> original2cloned) {

        //the cloned projects are outdated - they need to be updated by relinking
        final Collection<Job> outdated = original2cloned.values();

        /*
       * we calculate this function here and pass it on to the inner private relink function to save
       * it being computer every iteration of the function...
       * */
        final Map<String, String> nameMapping = RelinkingUtils.calcProjectNameMapping(original2cloned);

        for (Job job : outdated) {

            if (job instanceof AbstractProject) {

                AbstractProject project = (AbstractProject)job;
                relink(project, nameMapping, original2cloned);
            }
        }
    }

    private void relink(AbstractProject project, Map<String, String> nameMapping, Map<Job, Job> original2cloned) {

        if (project instanceof Project) {
            Project p = ((Project) project);
            relinkList(original2cloned, nameMapping, p.getBuildersList());
        }

        else if (project instanceof MatrixProject) {
            MatrixProject matrixProject = ((MatrixProject) project);
            relinkList(original2cloned, nameMapping, matrixProject.getBuildersList());
        }

        else if (project instanceof MavenModuleSet) {
            MavenModuleSet mavenProject = (MavenModuleSet) project;
            relinkList(original2cloned, nameMapping, mavenProject.getPrebuilders());
            relinkList(original2cloned, nameMapping, mavenProject.getPostbuilders());
        }

        Jenkins.getInstance().rebuildDependencyGraph();
    }

    private void relinkList(
            Map<Job, Job> original2cloned,
            Map<String, String> nameMapping,
            DescribableList buildSteps) {

        final Set<Job> originals = original2cloned.keySet();
        if (buildSteps != null) {

            final List<ConditionalBuilder> builders = buildSteps.getAll(ConditionalBuilder.class);
            for (ConditionalBuilder conditionalBuilder : builders) {

                final List<Builder> conditionalBuilders = conditionalBuilder.getConditionalbuilders();

                CopyArtifactsRelinker.relinkList(nameMapping, conditionalBuilders);
                TriggeredJobsRelinker.relinkList(original2cloned, conditionalBuilders);
            }

            List clones = new ArrayList(buildSteps.size());
            for (Object buildStep : buildSteps) {
                clones.add(buildStep);
            }

            buildSteps.clear();

            for (Object builder : clones) {

                Object buildStepToAdd = null;

                if (builder instanceof SingleConditionalBuilder) {

                    SingleConditionalBuilder conditionalBuilder = (SingleConditionalBuilder)builder;
                    final BuildStep buildStep = conditionalBuilder.getBuildStep();

                    if (buildStep instanceof CopyArtifact) {

                        buildStepToAdd =
                                new SingleConditionalBuilder(
                                        CopyArtifactsRelinker.createDuplicateRelinkedArtifactCopier(
                                            nameMapping,
                                            (CopyArtifact)buildStep
                                        ),
                                        conditionalBuilder.getCondition(),
                                        conditionalBuilder.getRunner()
                                );
                    }

                    //it's always TriggerBuilder and not BuildTrigger, because it'a a builder,
                    // not a publisher, because we're (conditional buildstep) a builder
                    else if (buildStep instanceof TriggerBuilder) {

                        TriggeredJobsRelinker.relinkBuilder(original2cloned, (TriggerBuilder)buildStep);
                        buildStepToAdd = builder;
                    }
                }

                else {

                    buildStepToAdd = builder;
                }

                try {
                    buildSteps.add(buildStepToAdd);
                } catch (IOException e) {
                    //TODO: add logging
                }
            }
        }
    }
}
