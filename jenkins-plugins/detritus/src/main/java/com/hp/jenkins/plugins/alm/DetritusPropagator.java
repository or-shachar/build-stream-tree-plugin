package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.model.Run;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/12/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusPropagator implements Propagator<Run> {
    @Override
    public Collection<Run> propagate(Run source) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
