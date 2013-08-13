package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.handler.Handler;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.scm.SCM;
import hudson.scm.SubversionRepositoryBrowser;
import hudson.scm.SubversionSCM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/7/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO should share logic with SvnScmGatheringHandler
 *
 * this class gets a mapping of old scm urls to updated scm urls, and when given a job it is expected to update the
 * scm urls according to the map.
 */
public class SvnScmChangingHandler implements Handler<Void,Job> {

    /**
     * the mapping to use when updating a given project's scm urls.
     */
    private Map<String, String> scmMappings;

    /**
     *
     * @param scmMappings the mapping to use when updating a given project's scm urls.
     */
    public SvnScmChangingHandler(Map<String, String> scmMappings) {

        this.scmMappings = scmMappings;
    }

    @Override
    public Void apply(Job node) {

        if (node instanceof AbstractProject) {

            final AbstractProject project = (AbstractProject) node;

            final SCM scm = project.getScm();

            //only SubversionSCM currently supported (TODO make this extensible)
            if (scm instanceof SubversionSCM) {
                apply(project);
            }
        }

        return null;

    }

    private void apply(AbstractProject project) {

        SubversionSCM scm = (SubversionSCM) project.getScm();

        final SubversionSCM.ModuleLocation[] locations = scm.getLocations();

        List<SubversionSCM.ModuleLocation> fixedLocations = new ArrayList(locations.length);

        //this will let us know if we need to change anything or not later on
        boolean fixNeeded = false;

        //for each url
        for (int i = 0 ; i < locations.length ; i++) {

            SubversionSCM.ModuleLocation location = locations[i];
            final String currentLocation = location.toString();
            if (this.scmMappings.containsKey(currentLocation)) {

                final String selectedLocation = this.scmMappings.get(currentLocation);
                //if it's the same - keep it as is
                if (currentLocation.equalsIgnoreCase(selectedLocation)) {
                    fixedLocations.add(location);
                }
                //otherwise use the updated version, and mark that we need to change configs
                else {
                    fixNeeded = true;
                    final SubversionSCM.ModuleLocation replacement =
                            new SubversionSCM.ModuleLocation(selectedLocation, location.getLocalDir());
                    fixedLocations.add(replacement);
                }
            }

            else {
                fixedLocations.add(location);
            }
        }

        if (fixNeeded) {

            try {
                project.setScm(
                    new SubversionSCM(
                            fixedLocations,
                            scm.getWorkspaceUpdater(),
                            (SubversionRepositoryBrowser)scm.getEffectiveBrowser(),
                            scm.getExcludedRegions(),
                            scm.getExcludedUsers(),
                            scm.getExcludedRevprop(),
                            scm.getExcludedCommitMessages(),
                            scm.getIncludedRegions())
                );
            }
            catch (IOException e) {
                throw new RuntimeException("failed to update scm to new configuration for project '" +
                        project.getFullDisplayName() + "'",e);
            }
        }
    }

}
