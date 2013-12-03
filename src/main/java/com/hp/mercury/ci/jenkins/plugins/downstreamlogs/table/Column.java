package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.BuildEntryFilter;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.ColumnRenderer;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.GroovyClassFactory;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 01:21
 * To change this template use File | Settings | File Templates.
 */
public class Column extends AbstractDescribableImpl<Column> {

    private StringProvider columnRenderer;
    private String header;
    private StringProvider js;
    private StringProvider filter;
    transient private GroovyClassFactory<ColumnRenderer> columnRendererFactory;
    transient private BuildEntryFilter buildEntryFilter;

    @DataBoundConstructor
    public Column(String header, StringProvider columnRenderer, StringProvider js, StringProvider filter) {

        this.header = header;
        this.columnRenderer = columnRenderer;
        this.js = js;
        this.filter = filter;
    }

    public BuildEntryFilter getBuildEntryFilter() {

        if (buildEntryFilter == null) {

            instantiateFilter();

            if (buildEntryFilter == null) {
                //potential infinite recursion, but i know this class works and exists so... :)
                this.filter = new StringProvider.DefaultStringProvider("TrueBuildEntryFilter.groovy");
                instantiateFilter();
            }
        }

        return buildEntryFilter;
    }

    private void instantiateFilter() {
        final String scriptContent = filter.toString();

        if (scriptContent != null && !scriptContent.isEmpty()) {
            try {

                buildEntryFilter = GroovyClassFactory.load(
                        BuildEntryFilter.class,
                        scriptContent).newInstance(null);
            }
            catch (Exception e) {
                Log.warning("failed to instantiate filter for column " + getHeader() + ": " + e.getMessage());
                Log.throwing(getClass().getName(),"Column",e);

            }
        }
    }

    public StringProvider getFilter() {
        return filter;
    }

    public void setFilter(StringProvider filter) {
        this.filter = filter;
    }

    public StringProvider getColumnRenderer() {
        return columnRenderer;
    }

    public void setColumnRenderer(StringProvider columnRenderer) {
        this.columnRenderer = columnRenderer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public StringProvider getJs() {
        return js;
    }

    public void setJs(StringProvider js) {
        this.js = js;
    }

    public GroovyClassFactory<ColumnRenderer> getColumnRendererFactory() {
        if (columnRendererFactory == null) {
            this.columnRendererFactory = GroovyClassFactory.<ColumnRenderer>load(ColumnRenderer.class, columnRenderer.toString());
        }
        return columnRendererFactory;
    }

    public void setColumnRendererFactory(GroovyClassFactory<ColumnRenderer> columnRendererFactory) {
        this.columnRendererFactory = columnRendererFactory;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Column> {

        @Override
        public String getDisplayName() {
            return "Rendered Column";
        }
    }

    public static List<? extends Descriptor<? extends Column>> all() {
        return Jenkins.getInstance().<Column, DescriptorImpl>getDescriptorList(Column.class);
    }

    @SuppressWarnings("unused")
    public static class CombinedColumn extends Column {

        private Collection<ColumnExtender> combinedColumns;

        @DataBoundConstructor
        public CombinedColumn(
                String header,
                StringProvider js,
                StringProvider filter,
                Collection<ColumnExtender> combinedColumns) {
            super(header,
                    //yes we could share the instance as a singleton, but as a hack we use the specific different instance to find the column instance from within the CombinedColumnRenderer
                    new StringProvider.DefaultStringProvider("CombinedColumnRenderer.groovy")
                    , js, filter);
            this.combinedColumns = combinedColumns;
        }

        public Collection<ColumnExtender> getCombinedColumns() {
            return combinedColumns;
        }

        public void setCombinedColumns(Collection<ColumnExtender> combinedColumns) {
            this.combinedColumns = combinedColumns;
        }

        @Extension
        public static class CombinedColumnDescriptorImpl extends DescriptorImpl {
            @Override
            public String getDisplayName() {
                return "Combined Column";
            }
        }
    }


}
