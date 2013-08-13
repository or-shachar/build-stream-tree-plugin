package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.criteria.InstanceOfCriteria;
import com.hp.commons.core.handler.impl.CastingHandler;
import hudson.Extension;
import hudson.model.*;
import jenkins.model.Jenkins;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/6/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Extension
public class DetritusBuild extends AbstractBuild<DetritusProject, DetritusBuild> {

    public DetritusBuild() throws IOException {
        super((DetritusProject)Jenkins.getInstance().getItem("not_supposed_to_get_here"));
    }

    public DetritusBuild(DetritusProject job) throws IOException {
        super(job);
    }

    public DetritusBuild(DetritusProject job, Calendar timestamp) {
        super(job, timestamp);
    }

    public DetritusBuild(DetritusProject project, File buildDir) throws IOException {
        super(project, buildDir);
    }

    @Override
    public void run() {
        super.run(new DetritusRunner());
    }

    public class DetritusRunner extends Run<DetritusProject, DetritusBuild>.Runner {

        @Override
        public void post(BuildListener listener) throws Exception {}

        @Override
        public void cleanUp(BuildListener listener) throws Exception {}

/* ---------------------------------
Interesting stuff starts here... everything above is just for jenkins extension sake...
 --------------------------------- */
        @Override
        public Result run(BuildListener listener) throws Exception, Run.RunnerAbortedException {

            DetritusListener.setInstance(listener);

            listener.getLogger().println("analyzing graph structure...");
            DetritusProcessGraph detritusGraph = getDetritusGraph(getBuild());

            listener.getLogger().println("determining failing tests...");
            FailedTests failingTests = detritusGraph.getFailingTests();

            listener.getLogger().println("determining revision ranges...");
            DetritusScmRevisionMapping revisionRanges = detritusGraph.getRevisionRanges();

            listener.getLogger().println("duplicating graph for execution purposes...");
            DetritusProcessGraph duplicateDetritusGraph = createDuplicateDetritusGraph(detritusGraph);

            listener.getLogger().println("starting test->revision mapping process...");
            detritus(duplicateDetritusGraph, revisionRanges, failingTests);

            return Result.SUCCESS;
        }

        private DetritusProcessGraph createDuplicateDetritusGraph(DetritusProcessGraph detritusGraph) {

            //TODO: actually clone the graph...
            return detritusGraph;
        }

        private DetritusProcessGraph getDetritusGraph(Run build) {

            final Collection<Cause> upstreamCauses = new ArrayList<Cause>(build.getCauses());
            CollectionUtils.filter(upstreamCauses, new InstanceOfCriteria(Cause.UpstreamCause.class));

            if (upstreamCauses.isEmpty()) {

                return new DetritusProcessGraphImpl(((DetritusBuild)build).getProject());
            }

            else {
                return new DetritusProcessGraphImpl(
                        CollectionUtils.map(
                                upstreamCauses,
                                new CastingHandler<Cause, Cause.UpstreamCause>(Cause.UpstreamCause.class)));
            }


        }





        public void detritus(DetritusProcessGraph graph, DetritusScmRevisionMapping revisionRanges, FailedTests failingTests) {

            DetritusListener.getInstance().getLogger().println("graph: " + graph + ".");
            DetritusListener.getInstance().getLogger().println("revision ranges: " + revisionRanges + ".");
            DetritusListener.getInstance().getLogger().println("failing tests: " + failingTests + ".");

        }
    }
}
