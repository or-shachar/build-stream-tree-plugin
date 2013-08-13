package com.hp.mercury.ci.jenkins.plugins.oo.core;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.string.StringUtils;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:40 PM
* To change this template use File | Settings | File Templates.
*/
public class OOServer {

    private String uniqueLabel;
    private String url;

    private String password;
    private String username;
    private boolean active;

    @Deprecated //keep this for backwards compatability
    public OOServer(String id, String hostUrl, String username, String password) throws Descriptor.FormException {

        this(id, hostUrl, username, password, true);

    }

    @DataBoundConstructor
    public OOServer(String id, String hostUrl, String username, String password, boolean active) throws Descriptor.FormException {

        this.uniqueLabel = id;
        this.url = hostUrl;
        this.username = username;
        this.password = password;
        this.active = active;

    }


    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public List<OOFlow> getFlows(String basepath) throws IOException {

        return CollectionUtils.map(
                OOAccessibilityLayer.listFlows(this, StringUtils.unslashifyPrefix(basepath).split("/")).getItems(),
                new Handler<OOFlow, OOListResponse.OOListItem>() {

                    public OOFlow apply(OOListResponse.OOListItem node) {
                        return new OOFlow(node.getValue());
                    }

                }
        );

    }

    public OOFlow getFlow(List<OOFlow> flows, final String id) throws IOException {

        List<OOFlow> copy = new ArrayList(flows);

        CollectionUtils.filter(copy, new Criteria<OOFlow>() {
            public boolean isSuccessful(OOFlow tested) {
                return tested.getId().equals(id);
            }
        });

        return (copy.isEmpty() ? null : copy.get(0));
    }

    public String getUniqueLabel() {
        return uniqueLabel;
    }

    public boolean isActive() {
        return active;
    }
}
