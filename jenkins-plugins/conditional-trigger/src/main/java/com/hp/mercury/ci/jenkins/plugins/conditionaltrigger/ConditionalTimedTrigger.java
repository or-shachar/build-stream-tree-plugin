package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger;

import antlr.ANTLRException;
import hudson.Extension;
import hudson.model.BuildableItem;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.triggers.TimerTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/3/12
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConditionalTimedTrigger extends Trigger<BuildableItem> {

    private TriggerCondition condition;

    public ConditionalTimedTrigger() {} //required for jenkins

    @DataBoundConstructor
    public ConditionalTimedTrigger(String spec, TriggerCondition condition) throws ANTLRException {
        super(spec);
        this.condition = condition;
    }

    public TriggerCondition getCondition() {
        return condition;
    }

    public void setCondition(TriggerCondition condition) {
        this.condition = condition;
    }

    @Override
    public void run() {

        if (job instanceof BuildableItem) {
            BuildableItem bi = (BuildableItem)job;
            if (this.condition.shouldTrigger(bi)) {
                bi.scheduleBuild(this.condition);
            }
        }
    }

    @Override
    public TriggerDescriptor getDescriptor() {
        return (DescriptorImpl)Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Extension
    public static class DescriptorImpl extends TriggerDescriptor {

        public List<? extends Descriptor<? extends TriggerCondition>> getTriggerConditions() {
            return TriggerCondition.all();
        }

        @Override
        public String getDisplayName() {
            return "Trigger According to Condition";
        }

        @Override
        public boolean isApplicable(Item item) {
            return item instanceof BuildableItem;
        }
    }
}
