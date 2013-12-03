package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.Column.CombinedColumn

def l = namespace(lib.LayoutTagLib)
def f = namespace(lib.FormTagLib)

f.entry(title:"Header", field:"header") {
    f.textbox(field: "header")
}

f.advanced() {

    f.entry(title: "Internal Column", field:"combinedColumns") {
        f.repeatableProperty(field:"combinedColumns", header:"Internal Column", add:"Add Internal Column")
    }

    f.entry(title:"Additional Javascript", field:"js") {
        f.dropdownDescriptorSelector(field: "js")
    }

    f.entry(title:"Filter", field:"filter") {
        f.dropdownDescriptorSelector(field: "filter")
    }
}

f.entry {
    l.div(align:"right") {
        f.repeatableDeleteButton(value: "Delete Column")
    }
}
