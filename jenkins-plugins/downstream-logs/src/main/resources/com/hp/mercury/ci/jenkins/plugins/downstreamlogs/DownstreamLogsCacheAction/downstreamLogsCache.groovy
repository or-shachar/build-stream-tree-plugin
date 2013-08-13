import lib.FormTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)

//*
l.table(id: "downstream-builds-cache") {
    //f.optionalBlock(title:"Downstream Builds Cache", checked:"false") {
    f.advanced(align:"left"){
        f.entry() {

            l.h3("Downstream Builds Cache")
            l.h4("Parser Configurations")
            my.parserConfigs.each {
                l.h6(it.toString())
            }

            l.br()
            l.h4("Downstream Builds")
            my.cachedEntries.each {
                l.h6(it.toString())
            }
        }
    }
}
//*/

