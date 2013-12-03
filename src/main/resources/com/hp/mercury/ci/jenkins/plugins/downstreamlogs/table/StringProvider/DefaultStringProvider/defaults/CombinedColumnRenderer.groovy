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
                    this.currentHeader = init.content.tableConf.columnExtenders[i].column.header
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

//    def traversedNames = new HashSet()

    def renderEntry(l, entry) {

        //can't do this in constructor, init.content.cols collection doesn't exist yet...
        initialize()

        //assign NOP
        //TODO: almost have it, need to use a proxy of l instead with td being NOOP instead
        //TODO: also need to change logic a bit so that combined Columns don't have to be visible in table...
            def (_, extender) = this.colsMap[this.currentHeader]
            extender.combinedColumns.each { colHeader ->
                def (subColumnRenderer, __) = this.colsMap[colHeader.toString()]
                subColumnRenderer.render(l, entry);
            }
    }

//    def recursiveRenderEntry(l, entry, headerOfColumnToRender) {
//
//        //avoid infinite cycles.
////        if (!traversedNames.contains(entry)) {
////            traversedNames.add(entry)
////        }
//
//
//    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {
        renderEntry(l, buildEntry)
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        renderEntry(l, jobEntry)
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        renderEntry(l, stringEntry)
    }

}
