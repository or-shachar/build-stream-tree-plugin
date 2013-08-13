package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.BuildEntryFilter
import hudson.matrix.MatrixBuild

public class MatrixSizeOneBuildEntryFilter implements BuildEntryFilter {

    public Boolean display(BuildStreamTreeEntry.BuildEntry e) {
        return !(e.run instanceof MatrixBuild &&
                e.run.exactRuns.size() == 1)
    }

    @Override
    Boolean display(BuildStreamTreeEntry.JobEntry e) {
        return true
    }

    @Override
    Boolean display(BuildStreamTreeEntry.StringEntry e) {
        return true
    }
}