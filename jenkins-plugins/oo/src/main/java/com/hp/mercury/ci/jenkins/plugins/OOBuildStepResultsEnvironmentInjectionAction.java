package com.hp.mercury.ci.jenkins.plugins;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.EnvironmentContributingAction;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 10/4/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class must remain in package com.hp.mercury.ci.jenkins.plugins for
 * backwards compatability
 */
public class OOBuildStepResultsEnvironmentInjectionAction implements EnvironmentContributingAction {

    private EnvVars injectedVars;

    public OOBuildStepResultsEnvironmentInjectionAction(EnvVars envVars) {
        this.injectedVars = envVars;
    }

    @Override
    public void buildEnvVars(AbstractBuild<?, ?> build, EnvVars env) {

        if (env != null && injectedVars != null) {
            env.putAll(injectedVars);
        }
    }

    /*
    These are nulls because this shouldn't be visible...
    */
    @Override
    public String getIconFileName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDisplayName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUrlName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
