package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.conditions;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.TriggerCondition;
import hudson.Extension;
import hudson.model.*;
import static hudson.model.Result.*;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/3/12
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class JobLastExecutionCondition extends TriggerCondition {

    private static DateFormat df = DateFormat.getInstance();

    //TODO this function exists in oobuildstep as well, unify in jenkins-commons
    public static final List<String> RESULTS = CollectionUtils.map(
            Arrays.asList(SUCCESS, UNSTABLE, FAILURE, NOT_BUILT, ABORTED),
            new Handler<String, Result>() {

                public String apply(Result node) {
                    return node.toString();
                }
            }
    );
    //TODO same as above
    public static List<String> possibleResults() {
        return RESULTS;
    }

    private int hoursSinceLastExecution;
    private String jobName;
    private ResultComparingLogic resultComparingLogic;

    private String result;

    public JobLastExecutionCondition() {} //required as extension point

    @DataBoundConstructor
    public JobLastExecutionCondition(
            String jobName,
            int hoursSinceLastExecution,
            String result,
            int resultComparingLogic) {

        this.jobName = jobName;
        this.hoursSinceLastExecution = hoursSinceLastExecution;
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
    public boolean shouldTrigger(BuildableItem irrelevant) {

        final TopLevelItem item = Jenkins.getInstance().getItem(this.jobName);
        if (item == null) {
            logger().warning("job " + jobName + " does not exist and so it's last execution can't be determined.");
            return false;
        }

        if (item instanceof AbstractProject) {

            Run lastBuild = getLastBuild((AbstractProject)item);

            return (lastBuild != null &&
                this.resultComparingLogic.resultAcceptable(lastBuild, Result.fromString(this.result)) &&
                getDiffInHours(lastBuild) < hoursSinceLastExecution);
        }

        return false;
    }

    private Run getLastBuild(AbstractProject job) {

        Run lastBuild = job.getLastBuild();

        while (lastBuild != null &&
                lastBuild.hasntStartedYet()) {

            lastBuild = lastBuild.getPreviousBuild();
        }

        return lastBuild;
    }

    private long getDiffInHours(Run lastBuild) {

        long differenceInMillis = System.currentTimeMillis() - lastBuild.getTime().getTime();
        long differenceInSeconds = differenceInMillis / 1000;

        return differenceInSeconds / (60*60);
    }

    @Override
    public String getShortDescription() {

        //this is a safe cast, we only get here if trigger succeeded,
        // and it only succeeds after validation that name is abstractproject
        final AbstractProject job = (AbstractProject)Jenkins.getInstance().getItem(this.jobName);

        return "job last execution condition matched: " +
                "last execution of " + this.jobName + " was on " + df.format(getLastBuild(job).getTime()) +
                "which is less than " + this.hoursSinceLastExecution + " hours ago.";
    }

    public int getHoursSinceLastExecution() {
        return hoursSinceLastExecution;
    }

    public void setHoursSinceLastExecution(int hoursSinceLastExecution) {
        this.hoursSinceLastExecution = hoursSinceLastExecution;
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

    @Extension
    public static class DescriptorImpl extends TriggerConditionDescriptor {

        @Override
        public String getDisplayName() {
            return "Last Execution Time of Job";
        }
    }
}
