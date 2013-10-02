package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */



public interface BuildEntryFilter {

    public Boolean display(BuildStreamTreeEntry.BuildEntry e);
    public Boolean display(BuildStreamTreeEntry.JobEntry e);
    public Boolean display(BuildStreamTreeEntry.StringEntry e);
}
