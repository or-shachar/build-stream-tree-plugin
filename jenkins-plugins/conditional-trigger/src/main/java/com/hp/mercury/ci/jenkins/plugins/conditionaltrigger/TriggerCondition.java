package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.*;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/3/12
 * Time: 1:42 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TriggerCondition extends Cause implements Describable<TriggerCondition> {

    abstract public boolean shouldTrigger(BuildableItem job);

    public static List<? extends Descriptor<? extends TriggerCondition>> all() {
        return Jenkins.getInstance().<TriggerCondition, TriggerConditionDescriptor>getDescriptorList(TriggerCondition.class);
    }



    protected Logger logger() {
        return Logger.getLogger(getClass().getName());
    };

    public static abstract class TriggerConditionDescriptor extends Descriptor<TriggerCondition> implements ExtensionPoint {
        public TriggerConditionDescriptor() { }

        public TriggerConditionDescriptor(Class<? extends TriggerCondition> clazz) {
            super(clazz);
        }
    }
}
