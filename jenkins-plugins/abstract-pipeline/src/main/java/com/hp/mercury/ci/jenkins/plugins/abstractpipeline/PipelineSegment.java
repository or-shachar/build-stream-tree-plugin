package com.hp.mercury.ci.jenkins.plugins.abstractpipeline;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 17/04/13
 * Time: 08:01
 * To change this template use File | Settings | File Templates.
 */
public class PipelineSegment extends AbstractDescribableImpl<PipelineSegment> {

    private String jobsContainedInSegment;
    private String segmentName;

    @DataBoundConstructor
    public PipelineSegment(String segmentName, String jobsContainedInSegment) {

        this.segmentName = segmentName;
        this.jobsContainedInSegment = jobsContainedInSegment;
    }

    public String getJobsContainedInSegment() {
        return jobsContainedInSegment;
    }

    public void setJobsContainedInSegment(String jobsContainedInSegment) {
        this.jobsContainedInSegment = jobsContainedInSegment;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<PipelineSegment> {

        @Override
        public String getDisplayName() {
            return "";
        }
    }
}
