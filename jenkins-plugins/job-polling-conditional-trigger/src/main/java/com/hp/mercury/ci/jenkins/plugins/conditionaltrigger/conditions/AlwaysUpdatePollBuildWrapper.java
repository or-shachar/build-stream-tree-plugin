package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.conditions;

import com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.ConditionalTimedTrigger;
import com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.TriggerCondition;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.BuildWrapper;
import hudson.triggers.Trigger;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/17/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlwaysUpdatePollBuildWrapper extends BuildWrapper {

    @DataBoundConstructor
    public AlwaysUpdatePollBuildWrapper() {}

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {

        //only if this execution is not caused by job poller
        if (build.getCause(JobPollCondition.class) == null) {

            final Trigger trigger = build.getProject().getTrigger(ConditionalTimedTrigger.class);

            if (trigger != null) {

                final TriggerCondition condition = ((ConditionalTimedTrigger)trigger).getCondition();

                if (condition != null && condition instanceof JobPollCondition) {

                    final JobPollCondition pollCondition = (JobPollCondition) condition;
                    final boolean jobPollCondition =
                            pollCondition.shouldTrigger(build.getProject());

                    if (jobPollCondition) {
                        listener.getLogger().println(
                                "updated polling trigger to " + pollCondition.getLastBuildNumber() +
                                " to prevent another polling execution");
                    }
                    else {
                        listener.getLogger().println(
                                "polling trigger last build number not updated as no new builds exist.");
                    }
                }
            }
        }

        //dignify contract
        return new Environment(){};
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<BuildWrapper> {

        public String getDisplayName() {
            return "Update Trigger Poller To Current Last Build";
        }

    }
}
