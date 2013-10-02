package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider

import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)

f.entry(title: "Path to File", field: "pathToFile") {
    f.textbox(field: "pathToFile", value: instance?.stringToProvide)
}