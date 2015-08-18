package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import groovy.lang.Binding;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.Jenkins;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 18/06/13
 * Time: 04:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class BuildStreamTreeEntry {

    @Deprecated
    transient private String path;
    @Deprecated
    transient private String template;

    public static class BuildEntry extends BuildStreamTreeEntry {

        transient Run run;
        private final String jobName;
        private final int buildNumber;

        public Run getRun() {
            if (this.run == null) {
                final Job job = Jenkins.getInstance().getItemByFullName(this.jobName, Job.class);
                if (job != null) {
                    this.run = job.getBuildByNumber(this.buildNumber);
                }
            }
            return this.run;
        }

        public BuildEntry(Run run) {
            this.run = run;

            this.jobName = this.run.getParent().getFullDisplayName();
            this.buildNumber = this.run.getNumber();
        }

        @Override
        public String toString() {
            return "BuildEntry{" +
                    "jobName='" + this.jobName + '\'' +
                    ", buildNumber=" + this.buildNumber +
                    '}';
        }

        public String getJobName() {
            return this.jobName;
        }

        public int getBuildNumber() {
            return this.buildNumber;
        }
    }

    public static class JobEntry extends BuildStreamTreeEntry {

        transient Job job;
        private final String jobName;

        public Job getJob() {
            if (this.job == null) {
                this.job = Jenkins.getInstance().getItemByFullName(this.jobName,Job.class);
            }
            return this.job;
        }

        public JobEntry(Job job) {

            this.job = job;
            this.jobName = job.getFullDisplayName();
        }

        public String getJobName() {
            return this.jobName;
        }

        @Override
        public String toString() {
            return "JobEntry{" +
                    "jobName='" + jobName + '\'' +
                    '}';
        }
    }

    public static class StringEntry extends BuildStreamTreeEntry {

        String string;

        public String getString() {
            return this.string;
        }

        public StringEntry(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return "StringEntry{" +
                    this.string +
                    '}';
        }
    }

}


