package com.hp.mercury.ci.jenkins.plugins.recursivecancelbuild;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.DownstreamLogsUtils;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import hudson.util.HttpResponses;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/06/13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class RecursiveCancelBuildAction implements Action {

    private transient final Run run;

    public RecursiveCancelBuildAction(Run target) {
        this.run = target;
    }

    public String getIconFileName() {

        return "/plugin/recursive-cancel-build/kill.png";
    }

    public String getDisplayName() {
        return "Cancel Build and Downstream";
    }

    public String getUrlName() {
        return "recursive-cancel";
    }

    public void doKill(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {

        if (Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER) ||
            (run.getParent() instanceof AbstractProject && run.getParent().hasPermission(AbstractProject.ABORT))) {

            recStop(run);
            rsp.sendRedirect2(Jenkins.getInstance().getRootUrl() + run.getParent().getUrl());
        }
        else {
            throw HttpResponses.forbidden();
        }
    }

    private void recStop(Run run) {
        if (run instanceof AbstractBuild && run.isBuilding()) {
            try {
                ((AbstractBuild)run).doStop();
            } catch (Exception ignored) {
            }
        }
        final Collection<BuildStreamTreeEntry> downstreamRuns = DownstreamLogsUtils.getDownstreamRuns(run);
        for (BuildStreamTreeEntry e : downstreamRuns) {
            if (e instanceof BuildStreamTreeEntry.BuildEntry) {
                recStop(((BuildStreamTreeEntry.BuildEntry) e).getRun());
            }
        }
    }

    public Run getRun() {
        return run;
    }
}
