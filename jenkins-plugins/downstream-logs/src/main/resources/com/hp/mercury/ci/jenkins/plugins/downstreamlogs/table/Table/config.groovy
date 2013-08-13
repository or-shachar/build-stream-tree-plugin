import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider

def l = namespace(lib.LayoutTagLib)
def f = namespace(lib.FormTagLib)

f.entry(title: "Configuration Name", field: "name") {
    f.textbox(field: "name")
}

f.advanced() {

    f.entry(title: "CSS", field: "css") {
        f.dropdownDescriptorSelector(field: "css")
    }

    f.entry(title: "JavaScript", field: "js") {
        f.dropdownDescriptorSelector(field: "js")
    }

    f.entry(title: "Groovy Initialization", field: "groovyInit") {
        f.dropdownDescriptorSelector(field: "groovyInit")
    }

    f.entry(title: "Groovy Additional Top Layout", field: "additionalTopLayout") {
        f.dropdownDescriptorSelector(field: "additionalTopLayout")
    }

    f.entry(title: "Groovy Row Filter", field: "rowDisplaymentEvaluator") {
        f.dropdownDescriptorSelector(field: "rowFilter")
    }

    f.entry(title: "Table Columns", field:"columns") {
        f.repeatableProperty(field:"columns", header:"Column", add:"Add Column")
    }
}
f.entry {
    l.div(align:"right") {
        f.repeatableDeleteButton(value: "Delete GUI Table Configuration")
    }
}
