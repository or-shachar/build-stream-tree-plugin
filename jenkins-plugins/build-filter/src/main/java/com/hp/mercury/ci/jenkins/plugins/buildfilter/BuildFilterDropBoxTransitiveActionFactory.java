package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 20/06/13
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
//we want the builds filter to be the lowest possible
@Extension(ordinal=Integer.MIN_VALUE)
public class BuildFilterDropBoxTransitiveActionFactory extends TransientProjectActionFactory {

    @Override
    public Collection<? extends Action> createFor(AbstractProject target) {
        return Collections.singletonList(new BuildFilterDropBoxAction(target));
    }
}
