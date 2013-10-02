package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer
import jenkins.model.Jenkins

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class SubtreeCollapserColumnRenderer implements ColumnRenderer {

    def content

    public SubtreeCollapserColumnRenderer(init) {
        this.content = init
    }

    //additional data for sorting
    def rowCounter = 0

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
        rowCounter--

        boolean isLeaf = this.content.findTreeNodeForBuildEntry(buildEntry).children.isEmpty()

        l.td(data:rowCounter, prefix:getNestingString(buildEntry)) {
            if (isLeaf) {
                l.text(" ")
            }
            else {
                l.div(expanded:"true", onClick: "expandCollapse(this)") {
                    l.img(src: "${Jenkins.instance.rootUrl}plugin/downstream-logs/images/16x16/minus.gif")
                }
            }

        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        rowCounter--
        l.td(data:rowCounter, prefix:getNestingString(jobEntry)) { l.text(" ") }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        rowCounter--
        l.td(data:rowCounter, prefix:getNestingString(stringEntry)) { l.text(" ") }
    }
}
