package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.Extension;
import hudson.model.*;
import jenkins.model.Jenkins;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 03/04/13
 * Time: 00:30
 * To change this template use File | Settings | File Templates.
 */
@Extension
public class DownstreamLogsActionFactory extends TransientBuildActionFactory {

    @Override
    public Collection<? extends Action> createFor(Run target) {

        ArrayList ret = new ArrayList(2);

        Job parent = target.getParent();
        if (target instanceof AbstractBuild) {

            /* see index.jelly in AbstractBuild: getActions returns summary items, not getTransitiveActions...
            if (    parent instanceof Project &&
                    ((Project) parent).getBuildWrappers().containsKey(
                            Jenkins.getInstance().getDescriptor(DownstreamLogsConfigurator.class))) {

                ret.add(new DownstreamLogsAction((AbstractBuild)target));
            }
            */

            ret.add(new DownstreamLogsSideMenuLink((AbstractBuild)target));
        }

        return ret;
    }


}
