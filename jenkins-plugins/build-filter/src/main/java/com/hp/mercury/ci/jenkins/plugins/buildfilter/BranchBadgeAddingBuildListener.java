package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 20/06/13
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
@Extension
public class BranchBadgeAddingBuildListener extends RunListener<Run> {

    @Override
    public void onStarted(Run run, @Nonnull TaskListener listener) {
        super.onStarted(run, listener);
        listener.getLogger().println("Adding branch badge to build");
        run.addAction(new BranchBadge());
    }
}
