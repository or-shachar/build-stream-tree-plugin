
def l = namespace(lib.LayoutTagLib)
def f = namespace(lib.FormTagLib)

f.entry(title:"Header", field:"header") {
    f.textbox(field: "header")
}

f.advanced() {
    f.entry(title:"Column Renderer", field:"columnRenderer") {
        f.dropdownDescriptorSelector(field: "columnRenderer")
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
