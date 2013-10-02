package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer
import hudson.Functions
import jenkins.model.Jenkins

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
class TestResultsLinksColumnRenderer implements ColumnRenderer {

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {

        def projectUrl = "$Jenkins.instance.rootUrl$buildEntry.run.parent.url"
        def buildUrl = "$projectUrl/$buildEntry.run.number"
        def testResults = buildEntry.run.testResultAction
        def testResultsUrl = "$buildUrl/${testResults?.urlName}"

        l.td(data: "2") {

            if (testResults == null) {

                l.text(" ")
            }

            else {

                l.a(href: testResultsUrl) {
                    l.img(src:"$Jenkins.instance.rootUrl$Functions.resourcePath/images/24x24/clipboard.png")
                }
            }
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {
        l.td(data: "1") {
            l.text(" ")
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        l.td(data: "0") { l.text(" ")}
    }
}
