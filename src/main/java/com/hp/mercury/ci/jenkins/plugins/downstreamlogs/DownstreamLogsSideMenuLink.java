package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Run;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 25/03/13
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
public class DownstreamLogsSideMenuLink implements Action {

    private Run build;

    public DownstreamLogsSideMenuLink(Run build) {
        this.build = build;
    }


    public String getIconFileName() {
        return "plugin.png";
    }

    public String getDisplayName() {
        return "Build Stream Tree";
    }

    public String getUrlName() {
        return "downstreamLogs";
    }

    public Run getBuild() {
        return build;
    }

    //used by jelly
    @SuppressWarnings("unused")
    public DownstreamLogsAction.DescriptorImpl getDescriptor() {
        return DownstreamLogsAction.getDescriptorStatically();
    }
}
