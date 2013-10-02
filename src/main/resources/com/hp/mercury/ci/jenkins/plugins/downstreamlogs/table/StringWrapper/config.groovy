package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringWrapper

import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)

f.textbox(field: "string")