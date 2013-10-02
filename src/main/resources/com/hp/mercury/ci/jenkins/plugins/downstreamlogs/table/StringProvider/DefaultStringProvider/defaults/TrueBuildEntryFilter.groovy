package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.BuildEntryFilter
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry


public class TrueBuildEntryFilter implements BuildEntryFilter {

    @Override
    public Boolean display(BuildStreamTreeEntry.BuildEntry e) {
        return true
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