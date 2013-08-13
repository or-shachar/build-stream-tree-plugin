package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.conditions;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.TriggerCondition;
import hudson.BulkChange;
import hudson.Extension;
import hudson.model.*;

import hudson.model.listeners.SaveableListener;
import jenkins.YesNoMaybe;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/3/12
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class JobPollCondition extends TriggerCondition {

    private String jobName;
    private String result;
    private ResultComparingLogic resultComparingLogic;
    private String thisJob;

    public JobPollCondition() {} //required as extension point

    @DataBoundConstructor
    public JobPollCondition(
            String thisJob,
            String jobName,
            String result,
            int resultComparingLogic) {

        this.thisJob = thisJob;
        this.jobName = jobName;
        this.result = result;
        this.resultComparingLogic = ResultComparingLogic.values()[resultComparingLogic];
    }

    public static Collection<String> possibleResultComparators() {
        return CollectionUtils.map(
                Arrays.asList(ResultComparingLogic.values()),
                new Handler<String, ResultComparingLogic>() {
                    public String apply(ResultComparingLogic node) {
                        return node.getDisplayName();
                    }
                });
    }

    @Override
    public synchronized boolean shouldTrigger(BuildableItem irrelevant)  {

        final TopLevelItem item = Jenkins.getInstance().getItem(this.jobName);
        if (item == null) {
            logger().warning("job " + jobName + " does not exist and so it can't be polled.");
            return false;
        }

        if (item instanceof AbstractProject) {

            final AbstractProject job = (AbstractProject) item;

            final Result threshold = Result.fromString(this.result);

            //variable that will contain a "newer" build than last polled (if exists) that we must verify passes comparator.
            final Run lastBuild  = getLastBuild(job);

            //while there are builds that we should check, starting from latest and moving back to earliest
            Run buildToCheckForCondition = lastBuild;
            while (isNewerThanLastPolled(buildToCheckForCondition)) {

                if (this.resultComparingLogic.resultAcceptable(
                        buildToCheckForCondition,
                        threshold)) {

                    setLastBuildNumber(lastBuild.getNumber());
                    return true;
                }

                buildToCheckForCondition = buildToCheckForCondition.getPreviousBuild();
            }

            //if there were new builds, but non matched the criteria - just update that we polled them.
            if (isNewerThanLastPolled(lastBuild)) {
                setLastBuildNumber(lastBuild.getNumber());
            }
        }

        return false;
    }

    /**
     *
     * @param build
     * @return true iff this build is greater than the last polled build
     */
    private boolean isNewerThanLastPolled(Run build) {

        final Integer lastBuildNumber = getLastBuildNumber();

        return
            //if it doesn't exist - it's not newer than our last polled, no matter what we polled.
            (build != null) && (
                //if this is our first poll operation, anything is newer than it is
                (lastBuildNumber == null) ||
                //if we've polled before, make sure not to pass our last polled number
                build.getNumber() > lastBuildNumber);
    }

    //TODO this is a duplicate from JobLastExecutionCondition
    private Run getLastBuild(AbstractProject job) {

        Run lastBuild = job.getLastBuild();

        while (lastBuild != null &&
                lastBuild.hasntStartedYet()) {

            lastBuild = lastBuild.getPreviousBuild();
        }

        return lastBuild;
    }

    @Override
    public String getShortDescription() {

        //this is a safe cast, we only get here if trigger succeeded,
        // and it only succeeds after validation that name is abstractproject
        final AbstractProject job = (AbstractProject)Jenkins.getInstance().getItem(this.jobName);

        Integer lastBuildNumber = getLastBuildNumber();

        return "job polling condition matched: " +
                this.jobName + " has a build that is " + this.getResultComparingLogic() + " " + this.result +
                " with build number which is greater than last polled value " + lastBuildNumber;
    }

    public synchronized Integer getLastBuildNumber() {

        File polledJobTrackerFile = getTrackerFile();
        Integer lastBuildNumber = null;

        BufferedReader is = null;
        try {
            is = new BufferedReader(new FileReader(polledJobTrackerFile));
            lastBuildNumber = Integer.parseInt(is.readLine().trim());
        }
        catch (IOException ioe) {
            //throw new RuntimeException(ioe);
        }
        finally {
            if (is != null) {
                try { is.close(); } catch(IOException ignored) {}
            }
        }

        return lastBuildNumber;
    }

    private File getTrackerFile() {
        final Job job = (Job) Jenkins.getInstance().getItemByFullName(thisJob);
        final File thisJobsDirectory = job.getConfigFile().getFile().getAbsoluteFile().getParentFile();

        return new File(thisJobsDirectory.getAbsolutePath() + "/" + jobName + "-tracker.txt");
    }

    public synchronized void setLastBuildNumber(Integer lastBuildNumber) {
        File polledJobTrackerFile = getTrackerFile();

        PrintStream stream = null;
        try {
            stream = new PrintStream(new FileOutputStream(polledJobTrackerFile, false));
            stream.println("" + lastBuildNumber);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public ResultComparingLogic getResultComparingLogic() {
        return resultComparingLogic;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Descriptor<TriggerCondition> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    //TODO: add rename listener, clonejobs relinker
    @Extension(dynamicLoadable = YesNoMaybe.YES)
    public static class DescriptorImpl extends TriggerConditionDescriptor {

        @Override
        public String getDisplayName() {
            return "New Build Available (job build poller)";
        }
    }

}
