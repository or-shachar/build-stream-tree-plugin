package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.graph.clone.Relinker;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.tasks.BuildTrigger;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO: this class (and the propagator) are inconsistent.
 * it looks at what is defined as downstream by the jenkins graph model, which can contain many things
 * due to different plugins but it only relinks the default "trigger downstream project" publisher.
 */
public class DownstreamProjectsRelinker implements Relinker<AbstractProject> {

    @Override
    public void relink(Map<AbstractProject, AbstractProject> original2cloned) {
        
        final Collection<AbstractProject> outdated = original2cloned.values();

        for (Job job : outdated) {

            if (job instanceof AbstractProject) {

                AbstractProject abstractProject = (AbstractProject)job;
                relink(abstractProject, original2cloned);
            }
        }
    }

    /**
     *
     * @param project that we wish to relink
     * @param original2cloned mapping from currently linked jobs to the jobs we want to be linked
     *
     * a lot of the code in this method is inspired by the doConfigSubmit method in AbstractProject.
     */
    private void relink(AbstractProject project, Map<AbstractProject, AbstractProject> original2cloned) {

        final Set<AbstractProject> formerlyLinkedProjects = original2cloned.keySet();

        //TODO: remove this and put it after all relinkers, in JobsCloner
        Jenkins.getInstance().rebuildDependencyGraph();

        //if the downstream projects contain any of the old projects, we should relink those.
        final List<AbstractProject> downstreamProjects = project.getDownstreamProjects();
        if (CollectionUtils.containsAny(downstreamProjects, formerlyLinkedProjects)) {

            //we only monitor the publishers list because "trigger other project" is a publisher
            DescribableList<Publisher,Descriptor<Publisher>> pl = project.getPublishersList();
            final List<BuildTrigger> allTriggers = pl.getAll(BuildTrigger.class);

            //this is in iteration form, but there can only be one "trigger downsteam project".
            //this just easily handles the trigger whether or not it exists
            for (BuildTrigger trigger : allTriggers) {

                final List<AbstractProject> childProjects = trigger.getChildProjects(project);
                final List<AbstractProject> replacements = RelinkingUtils.transferList(original2cloned, childProjects);

                try {
                    pl.remove(trigger);
                }
                catch (IOException e) {
                    throw new RuntimeException("failed to remove trigger for: " +
                        childProjects,e);
                }

                try {
                    pl.add(new BuildTrigger(replacements, trigger.getThreshold()));
                } catch (IOException e) {
                    throw new RuntimeException("failed to insert relinked trigger for: " +
                        replacements,e);
                }
            }
        }

        //TODO remove from here and put after all relinkers in JobsCloner
        Jenkins.getInstance().rebuildDependencyGraph();
    }


}
