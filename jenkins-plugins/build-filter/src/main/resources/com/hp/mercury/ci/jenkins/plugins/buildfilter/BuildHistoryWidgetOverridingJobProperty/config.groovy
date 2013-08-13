def l = namespace(lib.LayoutTagLib)
def f = namespace(lib.FormTagLib)

f.optionalBlock(
        name: "on",
        title: "Override job gui for parameter filtering on build history widget",
        inline:"true",
        checked:instance != null && instance.on) {
}
