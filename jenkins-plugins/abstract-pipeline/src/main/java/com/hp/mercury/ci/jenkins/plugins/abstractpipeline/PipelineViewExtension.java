package com.hp.mercury.ci.jenkins.plugins.abstractpipeline;

import hudson.Extension;
import hudson.model.*;
import hudson.util.DescribableList;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 16/04/13
 * Time: 22:14
 * To change this template use File | Settings | File Templates.
 */
public class PipelineViewExtension extends View {

    List<PipelineSegment> segments;

    @DataBoundConstructor
    public PipelineViewExtension(String name, List<PipelineSegment> segments) {
        super(name);
        this.segments = segments;
    }

    public List<PipelineSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<PipelineSegment> segments) {
        this.segments = segments;
    }

    @Override
    public Collection<TopLevelItem> getItems() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean contains(TopLevelItem item) {
        return false;
    }

    @Override
    public void onJobRenamed(Item item, String oldName, String newName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void submit(StaplerRequest req) throws IOException, ServletException, Descriptor.FormException {
        req.bindJSON(this, req.getSubmittedForm());
    }

    @Override
    public Item doCreateItem(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        return null;
    }


    @Extension
    public static class DescriptorImpl extends ViewDescriptor {

        @Override
        public String getDisplayName() {
            return "Abstract Pipeline View";
        }
    }
}
