import com.hp.mercury.ci.jenkins.plugins.buildfilter.BuildFilterDropBoxAction
import com.hp.mercury.ci.jenkins.plugins.buildfilter.BuildHistoryWidgetOverridingJobProperty
import hudson.Functions
import jenkins.model.Jenkins
import lib.LayoutTagLib

def overridingWidgetProperty = my.getProperty(BuildHistoryWidgetOverridingJobProperty.class)
if (overridingWidgetProperty?.isRelevant()) {

    def l = namespace(LayoutTagLib.class)
    def st = namespace("jelly:stapler")
    //<img width="24" height="24" style="margin: 2px;" alt="" src="${rootURL}${icon.startsWith('images/') || icon.startsWith('plugin/') ? h.resourcePath : ''}/${icon}"/>
    l.img(width:"24", height:"24", style:"margin: 2px;", alt:"", src:"${Jenkins.instance.rootUrl}plugin/build-filter/branch.png")
    l.select(id:"builds-filter", onChange:"refreshBuildHistory()") {

        l.option(value:"") {
            l.text("Select Branch")
        }

        def branchesSet = overridingWidgetProperty.branchesCache.branches

        branchesSet.each { branchName ->
            l.option(value: branchName) {
                l.text(branchName)
            }
        }
    }

    def dropboxAction = my.getAction(BuildFilterDropBoxAction.class)
    context.setVariable("relurl",Jenkins.instance.rootUrl + my.url)
    def imagesURL = Jenkins.instance.rootUrl + Functions.resourcePath + "/images"
    context.setVariable("imagesURL", imagesURL);
    st.include(it: dropboxAction, page:"onClick.jelly")

}



