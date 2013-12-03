package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class NestingCounterColumnRenderer implements ColumnRenderer {


    def content
    def NestingCounterColumnRenderer(init) {
        this.content = init
    }

    //additional data for sorting
    def rowCounter = 0

    Map cellMetadata(BuildStreamTreeEntry entry) {
        return [data: rowCounter--]
    }


    def recGetNestingString(node) {
        if (node == null) {
            return ""
        }
        return recGetNestingString(node.parent) + (node.index+1) + "."
    }

    def getNestingString(entry) {

        def node = this.content.findTreeNodeForBuildEntry(entry)

        return recGetNestingString(node)
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {
        l.text(getNestingString(buildEntry))
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        l.text(getNestingString(jobEntry))
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        l.text(getNestingString(stringEntry))
    }
}
