package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */


public interface ColumnRenderer {

    public void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry);
    public void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry);
    public void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry);
}
