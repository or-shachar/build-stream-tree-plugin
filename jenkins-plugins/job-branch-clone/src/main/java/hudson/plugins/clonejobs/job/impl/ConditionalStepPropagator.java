package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.Job;
import hudson.model.Project;
import hudson.tasks.BuildStep;
import hudson.tasks.Builder;
import hudson.util.DescribableList;
import org.jenkinsci.plugins.conditionalbuildstep.ConditionalBuilder;
import org.jenkinsci.plugins.conditionalbuildstep.singlestep.SingleConditionalBuilder;

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
 * TODO: this class should accept propagators in the constructor and use them instead of hardcoded
 * CopyArtifacts and TriggeringBuilds (what was i thinking???), or better yet, all other job-buildstep-propagators
 * should inherit from an abstract class that contains the different type of jobs, and includes conditional buildsteps logic.
 *
 * this class finds the propagations certain build steps of a job when they are contained in a conditional step.
 *
 */
public class ConditionalStepPropagator implements Propagator<Job> {

    @Override
    public Collection<Job> propagate(Job source) {

        final ArrayList<Job> ret = new ArrayList<Job>();

        /*TODO: refactor an abstract class where you implement just findTriggeringBuilders (rename the function btw)
        possibly also use boolean values to indicate which lists should be searched, or just search all of them.*/

        //this part delegates the relevant buildsteps for each type of project to the propagator logic
        if (source instanceof Project) {

            final Project project = (Project) source;
            findTriggeringBuilders(ret, project.getBuildersList());
        }

        if (source instanceof MavenModuleSet) {

            final MavenModuleSet project = (MavenModuleSet) source;
            findTriggeringBuilders(ret, project.getPrebuilders());
            findTriggeringBuilders(ret, project.getPostbuilders());
        }

        if (source instanceof MatrixProject) {

            final MatrixProject project = (MatrixProject) source;
            findTriggeringBuilders(ret, project.getBuildersList());
        }

        return ret;
    }

    /**
     *
     * @param ret the propagated jobs contained from within conditional steps.
     * @param buildSteps the buildsteps to search for conditional buildstep and propagations from within.
     */
    public void findTriggeringBuilders(
            Collection<Job> ret,
            DescribableList buildSteps) {

        if (buildSteps != null) {

            //handle the single buildstep condition
            final List<ConditionalBuilder> builders = buildSteps.getAll(ConditionalBuilder.class);
            for (ConditionalBuilder conditionalBuilder : builders) {

                final List<Builder> conditionalBuilders = conditionalBuilder.getConditionalbuilders();
                for (Builder b : conditionalBuilders) {

                    //TODO: refactor common propagation logic from both loops
                    CopyArtifactsPropagator.getPropagations(ret, b);
                    TriggeredProjectPropagator.getPropagations(ret, b);
                }
            }

            //handle multiple buildsteps conditions
            final List<SingleConditionalBuilder> singleConditionBuilders = buildSteps.getAll(SingleConditionalBuilder.class);
            for (SingleConditionalBuilder conditionalBuilder : singleConditionBuilders) {

                final BuildStep buildStep = conditionalBuilder.getBuildStep();

                CopyArtifactsPropagator.getPropagations(ret, buildStep);
                TriggeredProjectPropagator.getPropagations(ret, buildStep);
            }
        }
    }

}
