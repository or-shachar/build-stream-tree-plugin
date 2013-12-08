package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.model.Action;
import hudson.model.Job;
import jenkins.model.Jenkins;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 19/06/13
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
public class DownstreamLogsCacheAction implements Action {

    public List<ProjectAndBuildRegexParserConfig> parserConfigs;
    public List<BuildStreamTreeEntry> cachedEntries;

    public DownstreamLogsCacheAction(List<BuildStreamTreeEntry> entries) {
        this.setCachedEntries(entries);
    }

    public synchronized List<BuildStreamTreeEntry> getCachedEntries() {
        return cachedEntries;
    }

    public synchronized void setCachedEntries(List<BuildStreamTreeEntry> cachedEntries) {
        this.cachedEntries = cachedEntries;
        //make a note that this cache is only valid for a specific configuration. if they change we're invalid.
        this.parserConfigs = DownstreamLogsAction.getDescriptorStatically().getParserConfigs();
    }

    public synchronized List<ProjectAndBuildRegexParserConfig> getParserConfigs() {
        return parserConfigs;
    }

    //this is a hidden action
    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "Downstream Logs Cache";
    }

    public String getUrlName() {
        return "downstreamLogsCache";
    }

    public boolean allEntriesGiveSpecificBuild() {
        if (cachedEntries == null) {
            return false;
        }
        for (BuildStreamTreeEntry entry : cachedEntries) {
            if (!(entry instanceof BuildStreamTreeEntry.BuildEntry)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void updateEntries() {

        //bug fix in case of corrupt cacheEntries
        for (int i = 0 ; i < (cachedEntries == null ? 0 : cachedEntries.size()); i++) {

            BuildStreamTreeEntry entry = cachedEntries.get(i);

            if (entry instanceof BuildStreamTreeEntry.BuildEntry) {

                BuildStreamTreeEntry.BuildEntry buildEntry = (BuildStreamTreeEntry.BuildEntry)entry;
                if (buildEntry.getRun() == null) {
                    Job job = Jenkins.getInstance().getItemByFullName(buildEntry.getJobName(), Job.class);

                    //TODO sometimes we know the job instance and build number, but not the build, and we have no class for this...
                    entry = new BuildStreamTreeEntry.StringEntry(buildEntry.getJobName() + " #" + buildEntry.getBuildNumber());
                }
            }

            else if (entry instanceof BuildStreamTreeEntry.JobEntry) {
                BuildStreamTreeEntry.JobEntry jobEntry = (BuildStreamTreeEntry.JobEntry)entry;
                if (jobEntry.getJob() == null) {
                    entry = new BuildStreamTreeEntry.StringEntry(jobEntry.getJobName());
                }
            }

            cachedEntries.set(i, entry);
        }
    }
}
