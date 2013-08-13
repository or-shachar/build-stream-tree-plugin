package com.hp.mercury.ci.jenkins.plugins.dynamicgroovyview;

import groovy.lang.GroovyClassLoader;
import hudson.Extension;
import hudson.model.*;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.kohsuke.stapler.*;
import org.kohsuke.stapler.jelly.JellyFacet;
import org.kohsuke.stapler.jelly.groovy.GroovierJellyScript;
import org.kohsuke.stapler.jelly.groovy.GroovyClassTearOff;
import org.kohsuke.stapler.jelly.groovy.StaplerClosureScript;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/6/12
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unused")
public class DynamicGroovyView extends View {

    private String code;
    private String additionalClasspathJars;

    @SuppressWarnings("unused")
    public String getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public String getAdditionalClasspathJars() {
        return additionalClasspathJars;
    }

    @DataBoundConstructor
    public DynamicGroovyView(String name) {
        super(name);
    }

    @Override
    public Collection<TopLevelItem> getItems() {
        return Collections.emptyList();
    }

    @Override
    public boolean contains(TopLevelItem item) {
        return false;
    }

    @Override
    public void onJobRenamed(Item item, String oldName, String newName) {}

    @Override
    protected void submit(StaplerRequest req) throws IOException, ServletException, Descriptor.FormException {
        this.code = req.getSubmittedForm().getString("code");
        this.additionalClasspathJars = req.getSubmittedForm().getString("additionalClasspathJars");
    }

    //used dynamically to display the view
    @SuppressWarnings("unused")
    public void doDynamicGroovy(StaplerRequest req, StaplerResponse rsp) throws IOException, JellyTagException {

        final MetaClass metaClass = WebApp.getCurrent().getMetaClass(Hudson.getInstance());

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(StaplerClosureScript.class.getName());
        cc.setRecompileGroovySource(MetaClass.NO_CACHE);

        final GroovyClassLoader groovyClassLoader = new GroovyClassLoader(metaClass.classLoader.loader, cc);

        final StringBuffer requestURL = req.getRequestURL();
        URL url = URI.create(requestURL.toString()).toURL();
        final GroovierJellyScript script = new GroovierJellyScript(groovyClassLoader.parseClass(this.code), url);

        WebApp.getCurrent().getFacet(JellyFacet.class).scriptInvoker.invokeScript(req, rsp, script, Hudson.getInstance());
    }

    @Override
    public Item doCreateItem(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        return null;
    }

    @Extension
    public static final class DescriptorImpl extends ViewDescriptor {
        public String getDisplayName() {
            return "Dynamic Groovy View";
        }
    }
}
