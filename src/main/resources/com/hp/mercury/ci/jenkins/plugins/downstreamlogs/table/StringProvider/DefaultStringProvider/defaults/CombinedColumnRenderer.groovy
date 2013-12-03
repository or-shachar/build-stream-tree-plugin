package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log
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
    def column
    def initialized = false
    def subColRenderers

    def CombinedColumnRenderer(init) {

        this.init = init;
    }

    Map cellMetadata(BuildStreamTreeEntry entry) {

        initialize()

        def col = this.subColRenderers[0]
        def cellMetadata = col.metaClass.respondsTo(col, "cellMetadata", BuildStreamTreeEntry) ?
            col.cellMetadata(entry) : Collections.emptyMap();

        return cellMetadata
    }

    def initialize() {
        if (!initialized) {

            initialized = true;

            //all of this code just finds the column this renderer belongs to so that we can check its subcolumns.
            //TODO refactor, support infra to pass the column instance in the constructor optionally?

            //the assumption here is that the order of the rendereers cols is the same order as the columnExtenders
            def cols = init.content.cols

            //TODO would be simpler to iterate cols and columnExtenders together using transpose() on [cols, columnsExtenders]
            def i = 0
            for (colRenderer in cols) {

                if (colRenderer.is(this)) {
                    this.column = init.content.tableConf.columnExtenders[i].column
                    break
                }

                i++
            }

            //we know our implementation is of a CombinedColumn, so we can ask about combinedColumns directly
            this.subColRenderers = this.column.combinedColumns.collect { colExtender -> colExtender.column.columnRendererFactory.newInstance(init) }
            def l = init.content.l
            if (!init.content.emailMode) {
                this.column.combinedColumns.each { subColExt ->
                    def colJs = subColExt.column?.js.toString()
                    if (colJs != null && !colJs.isEmpty()) {
                        //TODO there should be a possible "column done" hook method where we can generate javascript,
                        // because sometimes it's important to call javascript after all of table is rendered.
                        l.script() {
                            l.raw(colJs)
                        }
                    }
                }
            }
        }
    }

    def renderEntry(l, entry) {

        //can't do this in constructor, init.content.cols collection doesn't exist yet...
        initialize()

        //assign NOP

        [column.combinedColumns, this.subColRenderers].transpose().each { subColExtender, subColRenderer ->
            def display = true;
            try {
                //TODO build entry filters don't get access to init object?
                display = subColExtender.column.buildEntryFilter.display(entry)
            }
            catch (Exception e) {
                Log.warning("failed to execute filter for ${column.header}:$subColExtender.column.header: $e.message")
                Log.throwing (
                        "/com/hp/mercury/ci/jenkins/plugins/downstreamlogs/DownstreamLogsSideMenuLink/content.groovy",
                        "render",
                        e)
            }
            if (display) {
                subColRenderer.render(l, entry)
            }
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
