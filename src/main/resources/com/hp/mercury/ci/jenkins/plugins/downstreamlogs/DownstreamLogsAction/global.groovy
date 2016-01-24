import lib.FormTagLib
import lib.JenkinsTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)
def j = namespace(JenkinsTagLib)

f.section(title: "Build Stream Tree") {

    f.entry(
            title: "Always embed build stream tree in builds",
            description: "check so that all new builds will automatically embed the build stream tree.",
            field: "alwaysEmbedBuildTree"
    ) {
        f.checkbox(field: "alwaysEmbedBuildTree")
    }

    f.entry(
            title: "Always embed build stream tree in job",
            description: "check so that all jobs will automatically embed the build stream tree.",
            field: "alwaysEmbedBuildTreeInJob"
    ) {
        f.checkbox(field: "alwaysEmbedBuildTreeInJob")
    }

    f.entry(title: "Cache build tree",
            description: "whenever a build is completed, scan the log and cache the downstream results.",
            field: "cacheBuilds"
    ) {
        f.checkbox(field: "cacheBuilds")
    }

    f.entry(title: "Enable Logging",
            field: "debugMode"
    ) {
        f.checkbox(field: "debugMode")
    }

    f.entry(title: "Default Email GUI Configuration",
            field: "emailTable"
    ) {
        f.textbox(field: "emailTable")
    }


    f.entry(
        title: "Log Parsing Regexes",
        description: "regex with 1 or 2 groups, where matches " +
                "to these groups are project name and build number from build log lines.",
        field: "parserConfigs"
    ) {
        //header = dragndrop
        f.repeatableProperty(field: "parserConfigs", header:"Parser Configuration")
    }

    f.entry(title: "Default Tree Depth",
            field: "treeDepth"
    ) {
        f.number(field: "treeDepth")
    }

    f.advanced() {
        f.entry(
                title: "GUI Table Configurations",
                field: "guiConfigurationTables"
        ) {
            //header = dragndrop
            f.repeatableProperty(field: "guiConfigurationTables", header:"GUI Table Configuration", add:"Add GUI Table Config")
        }
    }
}

