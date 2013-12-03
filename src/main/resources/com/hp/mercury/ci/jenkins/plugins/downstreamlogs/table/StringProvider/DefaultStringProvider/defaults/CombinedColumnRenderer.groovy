package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.Column
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class CombinedColumnRenderer implements ColumnRenderer {

    def init
    def currentHeader
    def colsMap
    def initialized = false

    def CombinedColumnRenderer(init) {

        this.init = init;

    }

    def initialize() {
        if (!initialized) {

            initialized = true;

            def cols = init.content.cols

            def i = 0
            for (colRenderer in cols) {

                if (colRenderer.is(this)) {
                    this.currentHeader = init.tableConf.columnExtenders[i].column.header
                    break
                }

                i++
            }

            this.colsMap = new HashMap()
            [cols, init.content.tableConf.columnExtenders].transpose().each {
                renderer, extender -> this.colsMap.put(extender.column.header, [renderer, extender.column])
            }
        }
    }

    def traversedNames = new HashSet<String>()

    def findCurrentColumnName() {

    }

    def recursiveEntry(l, entry, currentColumnName) {

        //can't do this in constructor, init.content.cols collection doesn't exist yet...
        initialize()

        //avoid infinite cycles.
        if (!traversedNames.contains(currentColumnName)) {
            traversedNames.add(currentColumnName)
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {
        recursiveRender(l, buildEntry, findCurrentColumnName())
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        recursiveRender(l, jobEntry, findCurrentColumnName())
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        recursiveRender(l, stringEntry, findCurrentColumnName())
    }

}
