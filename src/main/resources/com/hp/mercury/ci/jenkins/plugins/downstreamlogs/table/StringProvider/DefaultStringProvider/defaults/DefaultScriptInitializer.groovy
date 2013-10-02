package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider.defaults

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 09/07/13
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */

//content.groovy makes itself available to us via the binding
content = this.binding.getVariable("content.groovy")

staplerValue = null
def getStapler() {
    if (staplerValue == null) {
        staplerValue = content.namespace("jelly:stapler")
    }
    return staplerValue;
}

def findTreeNodeForBuildEntry(buildEntry) {
    return content.buildEntryToNodeMap.get(buildEntry)
}

return this