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

    Map cellMetadata(BuildStreamTreeEntry entry) {

        rowCounter--

        if ((entry instanceof BuildStreamTreeEntry.BuildEntry) || (entry instanceof BuildStreamTreeEntry.JobEntry)) {
            return [data: rowCounter, prefix:getNestingString(entry), jobName: entry.jobName, class: "counterTd"]
        }

        else if (entry instanceof BuildStreamTreeEntry.StringEntry) {
            return [data: rowCounter, prefix:getNestingString(entry), jobName: entry.string]
        }

        //never reach here...
        return Collections.emptyMap();
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {

        boolean isLeaf = this.content.findTreeNodeForBuildEntry(buildEntry).children.isEmpty()
        def buildEntryDepth = this.content.getBuildEntryDepth(buildEntry)
        String expandedValue = true
        String icon =  "minus.gif"

        if (isLeaf) {
            l.text(" ")
        }
        else {
            if(this.content.isExactlyTreeDepthLimit(buildEntry)){
                //if it is the lowest row that is displayed, it should be collapsed
                expandedValue= false;
                icon="plus.gif"
            }
            l.div(class: "expandCollapseDiv", expanded:expandedValue, onClick: "expandCollapse(this)", nodeDepth: "${buildEntryDepth}") {
                l.img(src: "${Jenkins.instance.rootUrl}plugin/downstream-logs/images/16x16/${icon}")
            }
        }

    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        l.text(" ")
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        l.text(" ")
    }
}
