package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 25/03/13
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
//this is now achieved via DownstreamLogsManualEmbedViaJobProperty
@Deprecated
public class DownstreamLogsConfigurator extends BuildWrapper {

    @DataBoundConstructor
    public DownstreamLogsConfigurator() {}

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException,
            InterruptedException {
        return new Environment() {};
    }

    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public String getDisplayName() {
            return "Embed Build Stream Tree in main build display";
        }

        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            //because this is deprecated
            return false;
        }


    }



}
