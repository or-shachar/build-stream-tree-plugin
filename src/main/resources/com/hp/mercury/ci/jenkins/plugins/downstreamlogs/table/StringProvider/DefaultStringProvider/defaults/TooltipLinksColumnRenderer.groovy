package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer
import jenkins.model.Jenkins

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
class TooltipLinksColumnRenderer implements ColumnRenderer {

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {

        def projectUrl = "$Jenkins.instance.rootUrl$buildEntry.run.parent.url"
        def buildUrl = "$projectUrl/$buildEntry.run.number"

        l.td(data: "2") {
            l.a(href: projectUrl, class: " model-link tl-tr ") {
                l.text("JOB")
            }

            l.raw(" ")

            l.a(href: buildUrl, class: " model-link tl-tr ") {
                l.text("BUILD")
            }
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        def projectUrl = "$Jenkins.instance.rootUrl$jobEntry.job.url"
        l.td(data: "1") {
            l.a(href: projectUrl, class: " model-link tl-tr ") {
                l.text("JOB")
            }
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        l.td(data: "0") { l.text(" ")}
    }
}
