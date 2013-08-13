package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.handler.Handler;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.scm.SCM;
import hudson.scm.SubversionSCM;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/7/12
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO should share logic with SvnScmChangingHandler
 *
 * this class gathers the SCM urls from jobs it visits and stores them in a predefined collection.
 */
public class SvnScmGatheringHandler implements Handler<Void, Job> {

    private Collection<String> scms;

    /**
     *
     * @param scms the collection in which to store urls for scms of visited jobs
     */
    public SvnScmGatheringHandler(Collection<String> scms) {
        this.scms = scms;
    }

    @Override
    public Void apply(Job node) {

        if (node instanceof AbstractProject) {

            final AbstractProject project = (AbstractProject) node;

            final SCM scm = project.getScm();
            //TODO only subversion supported (should be extensible)
            if (scm instanceof SubversionSCM) {
                apply((SubversionSCM)scm);
            }
        }

        return null;
    }

    private void apply(SubversionSCM scm) {

        final SubversionSCM.ModuleLocation[] locations = scm.getLocations();

        for (int i = 0 ; i < locations.length ; i++) {
            this.scms.add(locations[i].toString());
        }
    }
}
