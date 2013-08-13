package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.graph.clone.Relinker;
import hudson.adapters.PersistedListWrapper;
import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractProject;
import hudson.model.Project;
import hudson.plugins.copyartifact.CopyArtifact;
import hudson.util.DescribableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 5/21/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO relinker should use associated propagator somehow
 *
 * changes the CopyArtifact buildsteps to copy from the cloned project, and not the original.
 */
public class CopyArtifactsRelinker implements Relinker<AbstractProject> {

    @Override
    public void relink(Map<AbstractProject, AbstractProject> original2cloned) {

        final Collection<AbstractProject> clonedProjects = original2cloned.values();
        //get the string name map of original->clone project
        Map<String, String> projectNameMapping = RelinkingUtils.calcProjectNameMapping(original2cloned);

        for (AbstractProject clonedProject : clonedProjects) {
                relink(projectNameMapping, original2cloned, clonedProject);
        }
    }

    /**
     *
     * @param projectNameMapping original->cloned project names map
     * @param original2cloned   TODO - remove parameter
     * @param clonedProject project to relink
     *
     * TODO this should be done by some abstract class or something
     */
    private void relink(Map<String, String> projectNameMapping,
                        Map<AbstractProject, AbstractProject> original2cloned,
                        AbstractProject clonedProject) {

         DescribableList pl = null;

        if (clonedProject instanceof Project) {
             pl = ((Project) clonedProject).getBuildersList();
            relinkList(projectNameMapping, pl);
        }
        else if (clonedProject instanceof MavenModuleSet) {
            MavenModuleSet mavenProject = ((MavenModuleSet) clonedProject);
            relinkList(projectNameMapping, mavenProject.getPrebuilders());
            relinkList(projectNameMapping, mavenProject.getPostbuilders());
        }
        else if (clonedProject instanceof MatrixProject) {
            pl = ((MatrixProject) clonedProject).getBuildersList();
            relinkList(projectNameMapping, pl);
        }

    }

    /**
     * @param projectNameMapping original->clone project names mapping to use for updating pl
     * @param pl the buildstep list where we wish to update old references from "original" to "clone"
     *
     * the actual implementation accepts a List because that's a more common type and is used by other
     * relinkers as well, for example conditional-buildstep relinker.
     * this method exists as a comfort method that wraps the describable list pl with an actual list interface.
     * Adapter pattern, in short.
     */
    private void relinkList(Map<String, String> projectNameMapping, DescribableList pl) {
        relinkList(projectNameMapping, new PersistedListWrapper(pl));
    }

    /**
     * @param projectNameMapping original->clone project names mapping to use for updating pl
     * @param pl the buildstep list where we wish to update old references from "original" to "clone"
     */
    public static void relinkList(Map<String, String> projectNameMapping, List pl) {

        /*
        pl is already referenced by the project, so changing it's content directly simplifies the jenkins-handling.
        for this reason we create a copy list with which we will iterate, clear pl and add updated (as in
        original->clone) buildsteps to it.
         */

        //TODO put pl as an argument for ArrayList?
        //create a copy list
        List clones = new ArrayList();
        for (Object o : pl) {
            clones.add(o);
        }

        pl.clear();

        for (Object builder : clones) {

            //relink CopyArtifact buildsteps
            if (builder instanceof CopyArtifact) {

                CopyArtifact copyArtifactBuilder = (CopyArtifact)builder;

                CopyArtifact builderToUse = null;

                //if the currently iterated build step has a reference to a project that we cloned
                if (projectNameMapping.containsKey(copyArtifactBuilder.getProjectName())) {

                    //use a relinked version instead
                    builderToUse =
                        createDuplicateRelinkedArtifactCopier(projectNameMapping, copyArtifactBuilder);
                }

                //otherwise, use the same builder - there's nothing to update
                else {

                    builderToUse = copyArtifactBuilder;
                }

                pl.add(builderToUse);
            }

            //anything not a CopyArtifact buildstep we don't relink
            else {

                pl.add(builder);
            }
        }

    }

    /**
     *
     * @param projectNameMapping mapping to use for changing original project name to cloned project name
     * @param copyArtifactBuilder original copy artifact buildstep
     * @return newly allocated copy artifact buildstep relinked to the clone instead of the original.
     *
     * note: this function assumes that the current project configured in the copy-artifact was indeed cloned
     * and that this shows in the mapping.
     */
    public static CopyArtifact createDuplicateRelinkedArtifactCopier(
            Map<String, String> projectNameMapping,
            CopyArtifact copyArtifactBuilder) {

        String relinkedProjectName = projectNameMapping.get(copyArtifactBuilder.getProjectName());

        /*
        return new CopyArtifact(relinkedProjectName,
                copyArtifactBuilder.getBuildSelector(),
                copyArtifactBuilder.getFilter(),
                copyArtifactBuilder.getTarget(),
                copyArtifactBuilder.isFlatten(),
                copyArtifactBuilder.isOptional());
                */
        return new CopyArtifact(relinkedProjectName,
                copyArtifactBuilder.getParameters(),
                copyArtifactBuilder.getBuildSelector(),
                copyArtifactBuilder.getFilter(),
                copyArtifactBuilder.getTarget(),
                copyArtifactBuilder.isFlatten(),
                copyArtifactBuilder.isOptional());
    }
}
