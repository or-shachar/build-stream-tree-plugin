package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.BuildStreamTreeEntry
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.JenkinsLikeXmlHelper
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer
import hudson.Functions
import hudson.model.BallColor
import jenkins.model.Jenkins

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 08/07/13
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
class BallStatusColumnRenderer implements ColumnRenderer {

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.BuildEntry buildEntry) {
        //sort by image name
        def image = buildEntry.run.iconColor.image
        l.td(data: image) {
            l.img(src:"$Jenkins.instance.rootUrl$Functions.resourcePath/images/24x24/$image")
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.JobEntry jobEntry) {

        def image = BallColor.GREY.image
        l.td(data: image) {
            l.img(src:"$Jenkins.instance.rootUrl$Functions.resourcePath/images/24x24/${image}")
        }
    }

    @Override
    void render(JenkinsLikeXmlHelper l, BuildStreamTreeEntry.StringEntry stringEntry) {
        def image = BallColor.GREY.image
        l.td(data: image) {
            l.img(src:"$Jenkins.instance.rootUrl$Functions.resourcePath/images/24x24/${image}")
        }
    }
}
