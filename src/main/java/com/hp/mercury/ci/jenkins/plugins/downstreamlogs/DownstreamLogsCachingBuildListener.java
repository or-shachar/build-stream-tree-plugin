package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.Extension;
import hudson.model.*;
import hudson.model.listeners.RunListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 19/06/13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
//we want to run last, to make sure the log is complete before we exeecute...
@Extension(ordinal = Integer.MIN_VALUE + 10)
public class DownstreamLogsCachingBuildListener extends RunListener<Run> {

    @Override
    public void onFinalized(Run run) {
        super.onFinalized(run);

        final DownstreamLogsManualEmebedViaJobProperty property = (DownstreamLogsManualEmebedViaJobProperty)run.getParent().getProperty(DownstreamLogsManualEmebedViaJobProperty.class);
        boolean overridingEmbed = (property != null) && (property.getOverrideGlobalConfig()) && (property.getCacheBuild());
        if (DownstreamLogsAction.getDescriptorStatically().getCacheBuilds() || (overridingEmbed)) {
            Log.debug("Starting caching downstream builds for " + run.getFullDisplayName());
            DownstreamLogsUtils.getDownstreamRuns(run);
            Log.debug("Done caching downstream builds for " + run.getFullDisplayName());
        }

    }

    @Override
    public void onStarted(Run build, TaskListener listener) {

        DownstreamLogsAction downstreamLogsAction = build.getAction(DownstreamLogsAction.class);

        if (downstreamLogsAction == null) {
            downstreamLogsAction = new DownstreamLogsAction(build);
            build.addAction(downstreamLogsAction);
        }
    }
}
