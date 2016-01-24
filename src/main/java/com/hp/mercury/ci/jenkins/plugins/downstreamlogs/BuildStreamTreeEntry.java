package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

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

        public int compareTo(BuildStreamTreeEntry other) {
            if(!(other instanceof BuildEntry)){
                return 0;
            }

            Run thisRun = this.getRun();
            Run otherRun = ((BuildEntry) (other)).getRun();
            if(thisRun==null || otherRun==null){
                Log.warning("Tried to compare BuildStreamTreeEntry objects but at least one is null " +
                        "thisRun: "  + thisRun + "otherRun: " + otherRun);
                return 0;
            }

            long buildEntry1StartTime = thisRun.getStartTimeInMillis();
            long buildEntry2StartTime = otherRun.getStartTimeInMillis();
            if(buildEntry1StartTime < buildEntry2StartTime){
                return -1;
            }else if(buildEntry1StartTime == buildEntry2StartTime){
                //if start time and job name are the same, put the build with the lower number first
                Job thisRunParent = thisRun.getParent();
                Job otherRunParent = otherRun.getParent();
                if(thisRunParent==null || otherRunParent==null){
                    Log.warning("Tried to compare BuildStreamTreeEntry parent objects but at least one is null " +
                            "thisRun: "  + thisRunParent + "otherRun: " + otherRunParent);
                    return 0;
                }

                if(thisRunParent.getDisplayName().equals(otherRunParent.getDisplayName())){
                    return Integer.compare(this.getBuildNumber(), ((BuildEntry)(other)).getBuildNumber());
                }
                return 0;
            }else{
                return 1;
            }
        }
    }

    public static class JobEntry extends BuildStreamTreeEntry {

        transient Job job;
        private final String jobName;

        public Job getJob() {
            if (this.job == null) {
                this.job = Jenkins.getInstance().getItemByFullName(this.jobName, Job.class);
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

        public int compareTo(BuildStreamTreeEntry o) {
            return 0;
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

        public int compareTo(BuildStreamTreeEntry o) {
            return 0;
        }
    }

}


