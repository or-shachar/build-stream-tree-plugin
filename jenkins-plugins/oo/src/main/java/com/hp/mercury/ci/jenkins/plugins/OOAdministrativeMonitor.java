package com.hp.mercury.ci.jenkins.plugins;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import hudson.Extension;
import hudson.model.AdministrativeMonitor;

import java.util.Collection;
import java.util.Collections;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:32 PM
* To change this template use File | Settings | File Templates.
*/
@Extension
public final class OOAdministrativeMonitor extends AdministrativeMonitor {

    public static final String MONITOR_ID = "ooConfigMonitor";

    public OOAdministrativeMonitor() {
        super(MONITOR_ID);
        urlConfigurationErrors = Collections.emptyList();
    }

    private Collection<Exception> urlConfigurationErrors;

    public void setUrlConfigurationErrors(Collection<Exception> urlConfigurationErrors) {
        this.urlConfigurationErrors = urlConfigurationErrors;
    }

    public boolean isActivated() {
        return !urlConfigurationErrors.isEmpty();
    }

    public String getMessage() {

        //should never reach here
        if (urlConfigurationErrors == null) {
            return "unexpected problem in configuration of Operations Orchestration";
        }

        Collection<String> errors = CollectionUtils.map(urlConfigurationErrors,
                new Handler<String, Exception>() {

                    @Override
                    public String apply(Exception node) {

                        return node.getMessage() + "\n";
                    }
                });

        return "Configuration of HP Operations Orchestration failed, the following URLS were invalid: \r\n" + errors;
    }
}
