import com.hp.mercury.ci.jenkins.plugins.buildfilter.BranchAwareBuildHistoryWidget
import com.hp.mercury.ci.jenkins.plugins.buildfilter.BuildHistoryWidgetOverridingJobProperty
import jenkins.model.Jenkins

def l = namespace(lib.LayoutTagLib)
def descriptor = Jenkins.instance.getDescriptor(BuildHistoryWidgetOverridingJobProperty)

if (descriptor.showBranchBadges) {
    def branch = BranchAwareBuildHistoryWidget.getRunBranch(build)
    if (branch != null) {
        l.text(branch)
    }
}