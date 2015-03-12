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
public abstract class BuildStreamTreeEntry implements Comparable<BuildStreamTreeEntry> {

    @Deprecated
    transient private String path;
    @Deprecated
    transient private String template;

    public static class BuildEntry extends BuildStreamTreeEntry{

        transient Run run;
        private final String jobName;
        private final int buildNumber;

        public Run getRun() {
            if (run == null) {
                final Job job = Jenkins.getInstance().getItemByFullName(jobName, Job.class);
                if (job != null) {
                    this.run = job.getBuildByNumber(buildNumber);
                }
            }
            return run;
        }

        public BuildEntry(Run run) {
            this.run = run;

            this.jobName = this.run.getParent().getFullDisplayName();
            this.buildNumber = this.run.getNumber();
        }

        @Override
        public String toString() {
            return "BuildEntry{" +
                    "jobName='" + jobName + '\'' +
                    ", buildNumber=" + buildNumber +
                    '}';
        }

        public String getJobName() {
            return jobName;
        }

        public int getBuildNumber() {
            return buildNumber;
        }

        public int compareTo(BuildStreamTreeEntry other) {
            if(!(other instanceof BuildEntry)){
                return 0;
            }
            long buildEntry1StartTime = this.getRun().getStartTimeInMillis();
            long buildEntry2StartTime = ((BuildEntry)(other)).getRun().getStartTimeInMillis();
            if(buildEntry1StartTime < buildEntry2StartTime){
                return -1;
            }else if(buildEntry1StartTime == buildEntry2StartTime){
                //if start time and job name are the same, put the build with the lower number first
                if(this.getRun().getDisplayName().equals(((BuildEntry)(other)).getRun().getDisplayName())){
                    return Integer.compare(this.getBuildNumber(), ((BuildEntry)(other)).buildNumber);
                }
                return 0;
            }else{
                return -1;
            }
        }
    }

    public static class JobEntry extends BuildStreamTreeEntry {

        transient Job job;
        private final String jobName;

        public Job getJob() {
            if (job == null) {
                job = Jenkins.getInstance().getItemByFullName(jobName,Job.class);
            }
            return job;
        }

        public JobEntry(Job job) {

            this.job = job;
            this.jobName = job.getFullDisplayName();
        }

        public String getJobName() {
            return jobName;
        }

        @Override
        public String toString() {
            return "JobEntry{" +
                    "jobName='" + jobName + '\'' +
                    '}';
        }

        public int compareTo(BuildStreamTreeEntry o) {
            return 0;
        }
    }

    public static class StringEntry extends BuildStreamTreeEntry {

        String string;

        public String getString() {
            return string;
        }

        public StringEntry(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return "StringEntry{" +
                    string +
                    '}';
        }

        public int compareTo(BuildStreamTreeEntry o) {
            return 0;
        }
    }

}


