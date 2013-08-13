def f = namespace(lib.FormTagLib)
def l = namespace(lib.LayoutTagLib)

f.entry (title: "Configure segment name")
f.nested {
    f.textbox(field: "segmentName")
}

f.entry (title: "Select jobs that make up this pipeline segment")
f.nested{
    f.textbox(field: "jobsContainedInSegment")
}

f.entry(title: "") {
    l.div(align:"right") {
        f.repeatableDeleteButton()
    }
}

