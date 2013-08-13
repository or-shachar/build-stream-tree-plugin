package com.hp.jenkins.plugins.alm.propagators;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.model.AbstractProject;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpstreamProjectPropagator implements Propagator<AbstractProject> {

    private static UpstreamProjectPropagator instance;

    private UpstreamProjectPropagator() { }

    public static UpstreamProjectPropagator getInstance() {
        if (instance == null) {
            instance = new UpstreamProjectPropagator();
        }
        return instance;
    }

    @Override
    public Collection<AbstractProject> propagate(AbstractProject source) {
        return source.getUpstreamProjects();
    }
}
