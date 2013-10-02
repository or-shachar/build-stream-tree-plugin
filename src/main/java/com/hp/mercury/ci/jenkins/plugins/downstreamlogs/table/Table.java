package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.BuildEntryFilter;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.GroovyClassFactory;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.GroovyScriptFactory;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior.GroovyScriptFactoryImpl;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 01:21
 * To change this template use File | Settings | File Templates.
 */
public class Table extends AbstractDescribableImpl<Table> {

    public static final String TABLE_HTML_ID = "build-stream-tree-table";

    private String name;
    private List<Column> columns;
    private StringProvider css;
    private StringProvider js;
    private StringProvider rowFilter;
    private StringProvider groovyInit;
    private StringProvider additionalTopLayout;

    transient private GroovyClassFactory<BuildEntryFilter> rowFilterFactory;
    transient private GroovyScriptFactory<Object> initObjectFactory;
    transient private GroovyScriptFactory<Object> additionalTopLayoutFactory;

    @DataBoundConstructor
    public Table(String name,
                 List<Column> columns,
                 StringProvider css,
                 StringProvider js,
                 StringProvider groovyInit,
                 StringProvider additionalTopLayout,
                 StringProvider rowFilter) {

        this.name = name;
        this.columns = columns;
        this.css = css;
        this.js = js;
        this.groovyInit = groovyInit;
        this.rowFilter = rowFilter;
        this.additionalTopLayout = additionalTopLayout;
    }

    public GroovyClassFactory<BuildEntryFilter> getRowFilterFactory() {

        if (rowFilterFactory == null) {
            this.rowFilterFactory = GroovyClassFactory.load(BuildEntryFilter.class, this.rowFilter.toString());
        }

        return rowFilterFactory;
    }

    public GroovyScriptFactory<Object> getInitObjectFactory() {

        if (initObjectFactory == null) {

            String initScript = groovyInit.toString();

            this.initObjectFactory = initScript != null && !initScript.isEmpty() ?
                    GroovyScriptFactoryImpl.loadScript(Object.class, initScript) :
                    GroovyScriptFactoryImpl.nullFactory();
        }

        return initObjectFactory;
    }

    public GroovyScriptFactory<Object> getAdditionalTopLayoutFactory() {

        if (additionalTopLayoutFactory == null) {

            String additionalLayout = additionalTopLayout.toString();

            this.additionalTopLayoutFactory = additionalLayout != null && !additionalLayout.isEmpty() ?
                    GroovyScriptFactoryImpl.loadScript(Object.class, additionalLayout) :
                    GroovyScriptFactoryImpl.nullFactory();
        }

        return additionalTopLayoutFactory;
    }

    public StringProvider getAdditionalTopLayout() {
        return additionalTopLayout;
    }

    public void setAdditionalTopLayout(StringProvider additionalTopLayout) {
        this.additionalTopLayout = additionalTopLayout;
    }

    public void setAdditionalTopLayoutFactory(GroovyScriptFactory<Object> additionalTopLayoutFactory) {
        this.additionalTopLayoutFactory = additionalTopLayoutFactory;
    }

    public StringProvider getGroovyInit() {
        return groovyInit;
    }

    public void setGroovyInit(StringProvider groovyInit) {
        this.groovyInit = groovyInit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public StringProvider getCss() {
        return css;
    }

    public void setCss(StringProvider css) {
        this.css = css;
    }

    public StringProvider getJs() {
        return js;
    }

    public void setJs(StringProvider js) {
        this.js = js;
    }

    public StringProvider getRowFilter() {
        return rowFilter;
    }

    public void setRowFilter(StringProvider rowFilter) {
        this.rowFilter = rowFilter;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Table> {

        @Override
        public String getDisplayName() {
            return "GUI Table Descripor";
        }
    }
}
