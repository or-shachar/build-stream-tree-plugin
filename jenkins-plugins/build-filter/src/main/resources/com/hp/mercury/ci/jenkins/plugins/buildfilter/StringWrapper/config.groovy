import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)

f.textbox(field: "string")
l.div(align:"right") {
    f.repeatableDeleteButton()
}