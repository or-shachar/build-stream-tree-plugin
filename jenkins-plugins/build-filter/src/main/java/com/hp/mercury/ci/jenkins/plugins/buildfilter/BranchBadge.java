package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.model.BuildBadgeAction;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.Jenkins;

import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 20/06/13
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */

public class BranchBadge implements BuildBadgeAction {

    //these badges are serialized on the run instance, so it's ok to just keep the run it won't be "double serialized"
    // it will just give a reference to the top serialization run object
    @Deprecated
    private String jobName;

    @Deprecated
    private int buildNumber;

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }
}
