package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.combiner.CombinerFactory;
import com.hp.commons.core.combiner.impl.CriteriaResultAllSuccessfulCombiner;
import com.hp.commons.core.handler.collector.NonTransformingCollector;
import com.hp.commons.core.graph.impl.TraversalFactory;
import com.hp.jenkins.plugins.alm.propagators.UpstreamProjectPropagator;
import hudson.Extension;
import hudson.model.*;
import hudson.model.queue.CauseOfBlockage;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/6/12
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Extension
public class DetritusProject extends AbstractProject<DetritusProject, DetritusBuild> implements TopLevelItem {

    public DetritusProject() {
        super(Jenkins.getInstance(), "not_supposed_to_get_here");
    };

    protected DetritusProject(ItemGroup parent, String name) {
        super(parent, name);
    }

    @Override
    public DescribableList<Publisher, Descriptor<Publisher>> getPublishersList() {
        return new DescribableList<Publisher, Descriptor<Publisher>>(this);
    }

    @Override
    protected Class<DetritusBuild> getBuildClass() {
        return DetritusBuild.class;
    }

    @Override
    public boolean isFingerprintConfigured() {
        return false;
    }

    @Override
    protected void buildDependencyGraph(DependencyGraph graph) {
    }


    Map<AbstractProject, AbstractBuild> jobsWeHaveAlreadyWaitedForOnce =
            new HashMap<AbstractProject, AbstractBuild>();

    @Override
    /*
    * this will mean that if we have several triggering test jobs,
    * we will wait for them all to complete before running
    * */
    public CauseOfBlockage getCauseOfBlockage() {

        CauseOfBlockage causeOfBlockage = super.getCauseOfBlockage();

        if (causeOfBlockage == null) {

            final Set<AbstractProject> currentlyBuildingUpstreamTestProjects =
                    getCurrentlyBuildingUpstreamTestJobs();

            for (AbstractProject cbutp : currentlyBuildingUpstreamTestProjects) {

                final AbstractBuild lastBuild = (AbstractBuild)cbutp.getLastBuild();

                if (jobsWeHaveAlreadyWaitedForOnce.get(cbutp) == null) {

                    jobsWeHaveAlreadyWaitedForOnce.put(cbutp, lastBuild);
                }

                if (!jobsWeHaveAlreadyWaitedForOnce.get(cbutp).equals(lastBuild)) {

                    causeOfBlockage = new BecauseOfUpstreamBuildInProgress(
                        currentlyBuildingUpstreamTestProjects.iterator().next());

                    break;
                }
            }
        }

        return causeOfBlockage;
    }

    private Set<AbstractProject> getCurrentlyBuildingUpstreamTestJobs() {

        final Set<AbstractProject> collection = new HashSet<AbstractProject>();

        //collect test projects that are buildling
        TraversalFactory.getTraversal(

            //collect abstract projects that
            new NonTransformingCollector<AbstractProject>(

                    //combine a group of criterias...
                    CombinerFactory.combine(
                            Criteria.class,
                            Arrays.asList(

                                    //currently building criteria and test project criteria
                                    CurrentlyBuildingProjectCriteria.getInstance(),
                                    TestProjectCriteria.getInstance()
                                ),
                            new CriteriaResultAllSuccessfulCombiner()
                    ),

                    //into the collection
                    collection),

            //that are upstream of
            UpstreamProjectPropagator.getInstance()

        //this project
        ).apply(this);


        return collection;
    }

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public TopLevelItemDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    public static final class DescriptorImpl extends AbstractProjectDescriptor {
        public String getDisplayName() {
            return "Detritus Test Downstream Project";
        }

        public DetritusProject newInstance(ItemGroup parent, String name) {
            return new DetritusProject(parent,name);
        }

    }


    private static class CurrentlyBuildingProjectCriteria implements Criteria<AbstractProject> {

        private CurrentlyBuildingProjectCriteria() { }

        private static CurrentlyBuildingProjectCriteria instance;

        public static CurrentlyBuildingProjectCriteria getInstance() {
            if (instance == null) {
                instance = new CurrentlyBuildingProjectCriteria();
            }
            return instance;
        }

        @Override
        public boolean isSuccessful(AbstractProject tested) {
            return tested.isBuilding();
        }
    }
}
