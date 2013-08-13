package com.hp.mercury.ci.jenkins.plugins.trivials;


import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/6/12
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitBuildStep extends Builder {

    private int secondsToWait;

    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        return super.prebuild(build, listener);
    }

    @DataBoundConstructor
    public WaitBuildStep(int secondsToWait) {
        this.secondsToWait = secondsToWait;
    }

    public int getSecondsToWait() {
        return secondsToWait;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

        listener.getLogger().println("starting waiting for " + secondsToWait + " seconds.");
        Thread.currentThread().sleep(secondsToWait * 1000);
        listener.getLogger().println("done waiting for " + secondsToWait + " seconds.");

        return true;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public String getDisplayName() {
            return "Wait For X Seconds";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
