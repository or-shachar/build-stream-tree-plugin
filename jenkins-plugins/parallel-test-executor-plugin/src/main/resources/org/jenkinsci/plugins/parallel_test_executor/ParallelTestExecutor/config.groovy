package org.jenkinsci.plugins.parallel_test_executor.ParallelTestExecutor

import jenkins.model.Jenkins
import hudson.plugins.parameterizedtrigger.AbstractBuildParameters
import org.jenkinsci.plugins.parallel_test_executor.Parallelism

def f = namespace(lib.FormTagLib)

f.entry(title:"Test job to run",field:"testJob") {
    f.textbox()
}
f.entry(title:"Exclusion file name in the test job",field:"patternFile") {
    f.textbox()
}
f.entry(title:"Degree of parallelism", field:"parallelism") {
    f.hetero_radio(field:"parallelism", descriptors:Jenkins.instance.getDescriptorList(Parallelism.class))
}
f.entry(title:"Test report directory in the test job",field:"testReportFiles") {
    f.textbox()
}

f.entry(title:"instance: " + instance) {

}

f.advanced {
    f.block {
        f.hetero_list(name:"parameterConfigs",
                      hasHeader:"true",
                      descriptors:Jenkins.instance.getDescriptorList(AbstractBuildParameters.class),
                      oneEach:"true",
                      items:instance?.parameterConfigs,
                      addCaption:"Add Parameters")
    }

    f.entry(title:"Current job parameters", field:"currParams",
            help:"/plugin/parallel-test-executor-plugin/help-currParams.html") {
        f.checkbox(name:"currParams", checked:instance?.currParams, default:"true")
    }

    f.entry(title:"Exposed SCM", field:"exposedSCM",
            help:"/plugin/parallel-test-executor-plugin/help-repository.html") {
        f.checkbox(name:"exposedSCM", checked:instance?.exposedSCM, default:"true")
    }
}