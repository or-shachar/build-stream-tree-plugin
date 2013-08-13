package com.hp.jenkins.plugins.alm;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Run;
import jenkins.model.Jenkins;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class JenkinsUtils {

    public static AbstractBuild getBuild(Cause.UpstreamCause upstreamCause) {

        final AbstractProject upstreamProject =
                    (AbstractProject) Jenkins.getInstance().getItemByFullName(
                            upstreamCause.getUpstreamProject());

        //TODO is this casting ok? when is it not ok? when is a run not an abstarct-build?
        return (AbstractBuild)upstreamProject.getBuildByNumber(upstreamCause.getUpstreamBuild());
    }
}
