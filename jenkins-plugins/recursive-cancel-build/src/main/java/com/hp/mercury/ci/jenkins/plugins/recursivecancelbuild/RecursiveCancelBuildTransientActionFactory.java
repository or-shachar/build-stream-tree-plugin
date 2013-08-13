package com.hp.mercury.ci.jenkins.plugins.recursivecancelbuild;

import hudson.Extension;
import hudson.Functions;
import hudson.model.*;
import hudson.security.PermissionScope;
import jenkins.model.Jenkins;

import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/06/13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
@Extension
public class RecursiveCancelBuildTransientActionFactory extends TransientBuildActionFactory {

    @Override
    public Collection<? extends Action> createFor(Run target) {

        return target.isBuilding() &&
                (Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER) ||
                (target.getParent() instanceof AbstractProject && target.getParent().hasPermission(AbstractProject.ABORT))) ?

                Collections.singleton(new RecursiveCancelBuildAction(target)) :
                Collections.<Action>emptyList();
    }
}
