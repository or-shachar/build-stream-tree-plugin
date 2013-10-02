import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.ProjectAndBuildRegexParserConfig

import lib.FormTagLib
import lib.JenkinsTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)
def j = namespace(JenkinsTagLib)

f.entry(field: "groovyRegex", title: "Groovy Regex") {
    f.textbox()
}

f.entry(title: "Match Order") {
    l.select(class: "setting-input ", name: "regexParseOrder") {
        for (order in ProjectAndBuildRegexParserConfig.RegexParseOrder.values()) {

            if (instance?.regexParseOrder?.ordinal() == order.ordinal()) {
                l.option(
                        selected:"selected",
                        value:order.ordinal()
                ) {
                    l.text(order)
                }
            }
            else {
                l.option(
                        value:order.ordinal()
                ) {
                    l.text(order)
                }
            }
        }
    }
}

f.entry(title: "") {
    l.div(align:"right") {
        f.repeatableDeleteButton()
    }
}