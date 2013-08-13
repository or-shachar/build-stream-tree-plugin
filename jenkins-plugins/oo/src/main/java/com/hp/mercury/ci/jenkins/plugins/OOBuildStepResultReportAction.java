package com.hp.mercury.ci.jenkins.plugins;

import com.hp.mercury.ci.jenkins.plugins.oo.core.OORunRequest;
import com.hp.mercury.ci.jenkins.plugins.oo.core.OORunResponse;
import hudson.model.Action;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 10/4/12
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 *
 * this class must remain in package com.hp.mercury.ci.jenkins.plugins for
 * backwards compatability
 *
 */
public class OOBuildStepResultReportAction implements Action {

    private OORunResponse runResult;
    private OORunRequest runRequest;

    public OOBuildStepResultReportAction(OORunRequest request, OORunResponse ooBuildStepResult) {
        this.runRequest = request;
        this.runResult = ooBuildStepResult;
    }

    @Override
    public String getIconFileName() {
        //TODO use our own icon related to OO
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Build Step Result for " + runRequest.getFlow().getId();
    }

    @Override
    public String getUrlName() {

        return this.runResult.getReportUrl();
    }

}
