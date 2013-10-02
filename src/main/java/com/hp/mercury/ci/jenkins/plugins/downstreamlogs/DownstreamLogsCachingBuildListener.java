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
@Extension(ordinal = Integer.MIN_VALUE)
public class DownstreamLogsCachingBuildListener extends RunListener<Run> {

    @Override
    public void onFinalized(Run run) {
        super.onFinalized(run);

        if (DownstreamLogsAction.getDescriptorStatically().getCacheBuilds()) {
            final PrintStream printStream;
            try {
                printStream = new PrintStream(new FileOutputStream(run.getLogFile(), true));
                printStream.println("Caching downstream builds for Build Stream Tree");
                DownstreamLogsUtils.getDownstreamRuns(run);
                printStream.println("Caching completed");
                printStream.flush();
                printStream.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

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
