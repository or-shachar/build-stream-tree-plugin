import lib.FormTagLib
import lib.JenkinsTagLib
import lib.LayoutTagLib

def l = namespace(LayoutTagLib)
def f = namespace(FormTagLib)
def j = namespace(JenkinsTagLib)

f.section(title: "Builds Filter by Parameter") {

    f.entry(title: "Auto Override in jobs with Branch Parameter",
            field: "autoOverride",
            description: "build history filter works by replacing a section of jenkins job gui. that's why in each job you have to check (put a v) in a configuration to indicate that you want the gui overridden." +
                    "by checking this box, you're saying, nevermind that little checkbox in the gui - if the job has a parameter with one of the names specified here - override the GUI.") {
        f.checkbox(field: "autoOverride")
    }

    f.entry(title: "Show Branch Badges", field: "showBranchBadges", description:"for each entry in build history table show name of branch if exists") {
        f.checkbox(field: "showBranchBadges")
    }

    f.entry(title: "Ignore Hashes", field: "ignoreHashes", description:"while scanning for previous built branches to display in the combo-box, it's possible to filter out matches that are likely git-hashes (64 hexadecimal chars)") {
        f.checkbox(field: "ignoreHashes")
    }

    f.entry(
            title: "Repository Parameter Names",
            description: "many jobs have multiple different parameters with different names. here you put all the names of all variables for git repository you would like to filter the builds buy.",
            field: "repositoryParameterNames"
    ) {
        f.repeatableProperty(field: "repositoryParameterNames", header: "Repository Parameter")
    }

    //TODO: this should be made generic, there should be "dropdownlist config" where you can define as many dropdowns as you want and the parameters each filters by, and we should support all of these different boxes together when filtering, and we should use a regex to get only the bit of the string that we care about (repo name from repo path for example)
    f.entry(
            title: "Branch Parameter Names",
            description: "many jobs have multiple different parameters with different names. here you put all the names of all variables for git branch you would like to filter the builds buy.",
            field: "branchParameterNames"
    ) {
        f.repeatableProperty(field: "branchParameterNames", header: "Branch Parameter")
    }

}

