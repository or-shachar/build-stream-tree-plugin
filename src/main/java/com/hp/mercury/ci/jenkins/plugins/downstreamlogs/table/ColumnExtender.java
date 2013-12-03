package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 01:21
 * To change this template use File | Settings | File Templates.
 */
public class ColumnExtender extends AbstractDescribableImpl<ColumnExtender> {

    private Column column;

    @DataBoundConstructor
    public ColumnExtender(Column column) {

        this.column = column;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ColumnExtender> {

        @Override
        public String getDisplayName() {
            return "Column Extender";
        }
    }
}
