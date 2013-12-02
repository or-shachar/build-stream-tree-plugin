package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.DefaultStringProvider

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider
import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)
def dl = namespace("/lib/downstream-logs")

dl.dropdownList(title: "Select Default", name: "selectedDefaultName") {

    StringProvider.DefaultStringProvider.getDefaultsList().sort().each { defaultName ->

        context.setVariable("defaultText", StringProvider.DefaultStringProvider.getDefaultText(defaultName))

        f.dropdownListBlock(
                title: defaultName,
                value: defaultName,
                lazy: "defaultText",
                selected: instance?.selectedDefaultName?.equals(defaultName)
        ) {
            f.advanced() {
                f.entry() {
                    l.pre(defaultText)
                }
            }
        }
    }
}