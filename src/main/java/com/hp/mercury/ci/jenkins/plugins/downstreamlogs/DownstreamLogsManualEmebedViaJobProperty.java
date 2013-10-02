package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.*;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 25/03/13
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
public class DownstreamLogsManualEmebedViaJobProperty extends JobProperty {

    private Boolean embedInJob;
    private Boolean embedInBuild;
    private Boolean overrideGlobalConfig;

    public Boolean getEmbedInBuild() {
        return embedInBuild;
    }

    public void setEmbedInBuild(Boolean embedInBuild) {
        this.embedInBuild = embedInBuild;
    }

    public Boolean getOverrideGlobalConfig() {

        if (overrideGlobalConfig == null) {
            return getEmbedInBuild() || getEmbedInJob();
        }

        return overrideGlobalConfig;
    }

    public void setOverrideGlobalConfig(Boolean overrideGlobalConfig) {
        this.overrideGlobalConfig = overrideGlobalConfig;
    }

    public boolean getEmbedInJob() {

        return embedInJob;
    }

    public void setEmbedInJob(Boolean embedInJob) {
        this.embedInJob = embedInJob;
    }

    @DataBoundConstructor
    public DownstreamLogsManualEmebedViaJobProperty(
            Boolean overrideGlobalConfig,
            Boolean embedInJob,
            Boolean embedInBuild) {

        this.overrideGlobalConfig = overrideGlobalConfig;
        this.embedInJob = embedInJob;
        this.embedInBuild = embedInBuild;
    }

    //this doesn't work for matrix type builds, don't ask me why, probably really bad design..
    /*
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("performing");
        return true;
    }

    @Override
    public boolean prebuild(AbstractBuild build, BuildListener listener) {

        listener.getLogger().println("prebuilding");

        return true;
    }
    */

    @Initializer(after= InitMilestone.JOB_LOADED)
    public static void init() {

        try {
            if (!DownstreamLogsAction.getDescriptorStatically().getInitialized()) {

                for (Job job : Jenkins.getInstance().getItems(Job.class)) {
                    if (job.getProperty(DownstreamLogsManualEmebedViaJobProperty.class) == null) {
                        try {
                            //this handles backwards compatability
                            final String config = Util.loadFile(job.getConfigFile().getFile());
                            boolean active = config.contains(DownstreamLogsConfigurator.class.getSimpleName());

                            //we always add the job property, sometimes it's on, sometimes it's off.
                            job.addProperty(new DownstreamLogsManualEmebedViaJobProperty(active, active, active));
                        } catch (IOException e) {
                            Log.warning("failed to add DownstreamLogsManualEmebedViaJobProperty to job " + job.getFullDisplayName());
                            Log.throwing(DownstreamLogsManualEmebedViaJobProperty.class.getName(), "init", e);
                        }
                    }
                }

                DownstreamLogsAction.getDescriptorStatically().setInitialized(true);
            }
        }
        catch (Exception e) {
            Log.warning("failed to initialized downstream-logs overriding job properties");
            Log.throwing(DownstreamLogsManualEmebedViaJobProperty.class.getName(), "init", e);
        }
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {

        @Override
        public String getDisplayName() {
            return "Embed Build Stream Tree";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }
    }


    @SuppressWarnings("unused")
    public Run getBuild() {

        final boolean embed = getOverrideGlobalConfig() ?
                getEmbedInJob() :
                DownstreamLogsAction.getDescriptorStatically().getAlwaysEmbedBuildTreeInJob();

        return embed ?
                        owner.getLastBuild() :
                        null;
    }

}
