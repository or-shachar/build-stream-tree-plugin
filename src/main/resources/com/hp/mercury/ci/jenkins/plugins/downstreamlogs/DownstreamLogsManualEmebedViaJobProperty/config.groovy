
import hudson.model.Project
import jenkins.model.Jenkins

def f = namespace(lib.FormTagLib)

f.optionalBlock(
        name:"overrideGlobalConfig",
        title: "Override Build Stream Tree Global Embedding Configuration",
        inline:"true",
        checked:instance?.overrideGlobalConfig) {

    f.entry(field:"embedInJob", title:"Embed latest build stream tree in job") {
        f.checkbox(field: "embedInJob")
    }

    f.entry(field:"embedInBuild", title:"Embed build stream tree in new builds") {
        f.checkbox(field: "embedInBuild")
    }

    f.entry(field:"cacheBuild", title:"Cache build downstream triggers on build completion") {
        f.checkbox(field: "cacheBuild")
    }
}