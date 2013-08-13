package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 28/06/13
* Time: 01:16
* To change this template use File | Settings | File Templates.
*/
public class StringWrapper extends AbstractDescribableImpl<StringWrapper> {

    private String string;

    @DataBoundConstructor
    public StringWrapper(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String toString() {
        return this.string;
    }

    @Extension
    public static class StringWrapperDescriptor extends Descriptor<StringWrapper> {

        @Override
        public String getDisplayName() {
            return "string wrapper descriptor";
        }
    }
}
