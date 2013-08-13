
def f = namespace(lib.FormTagLib)

f.entry(title: "Configure Pipeline Segments")
f.nested {
    f.repeatableProperty(field: "segments", header: "Segment")
}

