package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider.TextareaStringProvider

import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)

f.entry(title: "Insert Script", field: "stringToProvide") {
    f.textarea(field: "stringToProvide")
}
