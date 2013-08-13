package com.hp.jenkins.plugins.alm.propagators;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpstreamBuildPropagator implements Propagator<AbstractBuild> {

    private static UpstreamBuildPropagator instance;

    private UpstreamBuildPropagator() { }

    public static UpstreamBuildPropagator getInstance() {
        if (instance == null) {
            instance = new UpstreamBuildPropagator();
        }
        return instance;
    }

    @Override
    public Collection<AbstractBuild> propagate(AbstractBuild source) {


        //upstream builds are returned as project number pairs
        final Map<AbstractProject, Integer> upstreamBuilds = source.getUpstreamBuilds();

        //so we turn them into actual build instances.
        return CollectionUtils.map(upstreamBuilds.entrySet(),

            new Handler<AbstractBuild, Map.Entry<AbstractProject, Integer>>() {

                @Override
                public AbstractBuild apply(Map.Entry<AbstractProject, Integer> node) {

                    final Integer buildNumber = node.getValue();
                    final AbstractProject project = node.getKey();

                    return (AbstractBuild)project.getBuildByNumber(buildNumber);
                }
            }
        );
    }
}
