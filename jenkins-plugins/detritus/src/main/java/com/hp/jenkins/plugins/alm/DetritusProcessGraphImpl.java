package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.handler.collector.NonTransformingCollector;
import com.hp.commons.core.handler.collector.TransformingCollector;
import com.hp.commons.core.handler.impl.ChainHandler;
import com.hp.commons.core.handler.impl.FilteringHandler;
import com.hp.commons.core.graph.impl.traversal.MultipleSourcesTraversalImpl;
import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;
import com.hp.commons.core.graph.impl.TraversalFactory;
import com.hp.commons.core.graph.traverser.Traversal;
import com.hp.jenkins.plugins.alm.propagators.UpstreamBuildPropagator;
import com.hp.jenkins.plugins.alm.propagators.UpstreamProjectPropagator;
import hudson.model.*;
import hudson.scm.SvnHelper;
import hudson.tasks.test.AbstractTestResultAction;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/9/12
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */

public class DetritusProcessGraphImpl implements DetritusProcessGraph {

    private Collection<DetritusBuildWrapper> collectedScmBuilds;
    private Collection<DetritusBuildWrapper> collectedTestBuilds;

    public DetritusProcessGraphImpl(DetritusProject project) {

        this.collectedTestBuilds = collectTests(project);

        this.collectedScmBuilds = collectScms(this.collectedTestBuilds);
    }

    public DetritusProcessGraphImpl(Collection<Cause.UpstreamCause> upstreamCauses) {

        this.collectedTestBuilds = collectTests(upstreamCauses);

        this.collectedScmBuilds = collectScms(this.collectedTestBuilds);
    }

    private Collection<DetritusBuildWrapper> collectScms(Collection<DetritusBuildWrapper> collectedTestBuilds) {

        final Map<AbstractProject, DetritusBuildWrapper> latestScmBuildsWithUnstableDownstreams = mergeScmBuildsByComparator(
                collectedTestBuilds,
                new Comparator<AbstractBuild>() {

                    @Override
                    public int compare(AbstractBuild currentlySelectedBuild, AbstractBuild potentiallyOlderBuild) {
                        return potentiallyOlderBuild.getNumber() - currentlySelectedBuild.getNumber();
                    }
                }
        );

        return latestScmBuildsWithUnstableDownstreams.values();
    }

    private Collection<DetritusBuildWrapper> collectTests(Collection<Cause.UpstreamCause> upstreamCauses) {

        final List<AbstractBuild> builds = CollectionUtils.map(
                upstreamCauses,
                new Handler<AbstractBuild, Cause.UpstreamCause>() {
                    @Override
                    public AbstractBuild apply(Cause.UpstreamCause node) {
                        return JenkinsUtils.getBuild(node);
                    }
                });

        final NonTransformingCollector<AbstractBuild> testCollector =
            new NonTransformingCollector<AbstractBuild>(
                    TestBuildCriteria.getInstance()
            );

        final Traversal<Void, Void, AbstractBuild> traversal =
                TraversalFactory.getTraversal(
                        testCollector,
                        UpstreamBuildPropagator.getInstance());


        new MultipleSourcesTraversalImpl(
            new NonRepeatingTraversal(traversal)).
                traverse(builds);

        return CollectionUtils.map(
            testCollector.getCollection(),
            new Handler<DetritusBuildWrapper, AbstractBuild>() {

                @Override
                public DetritusBuildWrapper apply(AbstractBuild node) {

                    return DetritusBuildWrapperFactory.getInstance().apply(node);
                }
            }
        );
    }

    private Collection<DetritusBuildWrapper> collectTests(DetritusProject dp) {

        final NonTransformingCollector<AbstractProject> testCollector =
            new NonTransformingCollector<AbstractProject>(
               TestProjectCriteria.getInstance()
            );

        final Traversal<Void, Void, AbstractProject> traversal =
                TraversalFactory.getTraversal(
                        testCollector,
                        UpstreamProjectPropagator.getInstance());

        traversal.apply(dp);

        return CollectionUtils.map(
                testCollector.getCollection(),
                new Handler<DetritusBuildWrapper, AbstractProject>() {
                    @Override
                    public DetritusBuildWrapper apply(AbstractProject node) {

                        return DetritusBuildWrapperFactory.getInstance().
                                apply(node.getLastBuild());
                    }
                }
        );

    }

    public DetritusScmRevisionMapping getRevisionRanges() {

        final Collection<DetritusBuildWrapper> scmBuilds = getScmProjects();

        final Map<AbstractProject, DetritusBuildWrapper> latestScmBuildsWithoutTestFailures =
                getLatestScmBuildsWithoutTestFailures(getFailingTests());

        DetritusScmRevisionMapping ret = new DetritusScmRevisionMapping();

        for (DetritusBuildWrapper scmBuild : scmBuilds) {

            //TODO change API to AbstractBuild and not Run so as to remove this casting
            final AbstractBuild run = (AbstractBuild)scmBuild.getJobBuild();

            final AbstractProject project = run.getProject();

            final Map<String, Long> lastRevisions =
                    SvnHelper.extractRepoVersionMapping(run);

            final Run scmBuildsWithoutTestFailure =
                    latestScmBuildsWithoutTestFailures.get(project).getJobBuild();

            final Map<String, Long> firstRevisions =
                    scmBuildsWithoutTestFailure == null ?
                        null :
                        SvnHelper.extractRepoVersionMapping(scmBuildsWithoutTestFailure);

            ret.add(scmBuild, firstRevisions, lastRevisions);
        }

        return ret;
    }

    private Map<AbstractProject, DetritusBuildWrapper> getLatestScmBuildsWithoutTestFailures(FailedTests failedTests) {

        return mergeScmBuildsByComparator(

            getLatestStableBuildsPerProject(failedTests).values(),

            new Comparator<AbstractBuild>() {
                @Override
                public int compare(AbstractBuild currentlyCollectedBuild, AbstractBuild potentialReplacement) {
                    return currentlyCollectedBuild.getNumber() - potentialReplacement.getNumber();
                }
            }
        );
    }

    private Map<AbstractProject, DetritusBuildWrapper> mergeScmBuildsByComparator(
                Collection<DetritusBuildWrapper> buildsDownstreamToScmsThatShouldBeMerged,
                Comparator<AbstractBuild> comparator) {

        final Map<AbstractProject, DetritusBuildWrapper> collectedBuilds =
                new HashMap<AbstractProject, DetritusBuildWrapper>();

        //find the last build that didn't contain a test failure for all tests
        //Map.Entry<AbstractProject, Run> entry : getLatestStableBuildsPerProject(failedTests).entrySet()
        for (DetritusBuildWrapper wrapper : buildsDownstreamToScmsThatShouldBeMerged) {

            //TODO refactor Run -> AbstractBuild so i won't have to cast
            AbstractBuild run = (AbstractBuild)wrapper.getJobBuild();

            Collection<DetritusBuildWrapper> scmBuilds = getScmBuildsUpstreamToBuild(run);

            for (DetritusBuildWrapper scmBuildWrap : scmBuilds) {

                //TODO refactor Run -> AbstractBuild so i won't have to cast
                final AbstractBuild scmBuild = (AbstractBuild)scmBuildWrap.getJobBuild();
                final AbstractProject scmProject = scmBuild.getProject();

                //TODO refactor Run -> AbstractBuild so i won't have to cast
                final DetritusBuildWrapper detritusBuildWrapper = collectedBuilds.get(scmProject);
                if (detritusBuildWrapper != null) {

                    final AbstractBuild currentlyCollectedBuild = (AbstractBuild) detritusBuildWrapper.getJobBuild();
                    if (comparator.compare(currentlyCollectedBuild, scmBuild) > 0) {

                        collectedBuilds.put(scmProject, scmBuildWrap);
                    }
                }
            };
        }

        return collectedBuilds;
    }

    private Collection<DetritusBuildWrapper> getScmBuildsUpstreamToBuild(AbstractBuild run) {
        Collection<DetritusBuildWrapper> scmBuilds = new ArrayList<DetritusBuildWrapper>();

        TraversalFactory.<Void, AbstractBuild>getTraversal(
                new TransformingCollector<DetritusBuildWrapper, AbstractBuild>(
                        new ChainHandler<DetritusBuildWrapper, Run, AbstractBuild, AbstractBuild>(
                            DetritusBuildWrapperFactory.getInstance(),
                            new FilteringHandler<AbstractBuild>(
                                    ScmBuildCriteria.getInstance())
                            ),
                        scmBuilds),
                UpstreamBuildPropagator.getInstance()
        ).apply(run);
        return scmBuilds;
    }

    private Map<AbstractProject, DetritusBuildWrapper> getLatestStableBuildsPerProject(FailedTests failedTests) {

        Map<AbstractProject, DetritusBuildWrapper> latestStableBuilds = new HashMap<AbstractProject, DetritusBuildWrapper>();

        final Collection<FailedTestBuildConfiguration> failedTestBuildConfigurations = failedTests.get();

        for (FailedTestBuildConfiguration config : failedTestBuildConfigurations) {

            final AbstractProject project = ((Build) config.getBuild().getJobBuild()).getProject();

            Run lastStableBuild = project.getLastStableBuild();
            if (lastStableBuild == null) {

                lastStableBuild = project.getFirstBuild();
                DetritusListener.getInstance().error("could not find latest stable build of " + project +
                        " using last existing record instead...");
            }

            DetritusListener.getInstance().getLogger().println("using last stable build: " + lastStableBuild);

            latestStableBuilds.put(project, DetritusBuildWrapperFactory.getInstance().apply(lastStableBuild));
        }

        return latestStableBuilds;
    }

    public FailedTests getFailingTests() {

        final FailedTests failedTests = new FailedTests();

        final Collection<DetritusBuildWrapper> testBuilds = getTestBuilds();
        for (DetritusBuildWrapper testBuild : testBuilds) {

            final AbstractTestResultAction testResults = testBuild.getJobBuild().getAction(AbstractTestResultAction.class);

            if (testResults != null) {
                failedTests.put(testBuild, testResults);
            }
        }

        return failedTests;
    }

    @Override
    public Collection<DetritusBuildWrapper> getTestBuilds() {

        return this.collectedTestBuilds;
    }

    @Override
    public Collection<DetritusBuildWrapper> getScmProjects() {

        return this.collectedScmBuilds;
    }

    @Override
    public String toString() {
        return "DetritusProcessGraphImpl{" +
                "collectedScmBuilds=" + collectedScmBuilds +
                ", collectedTestBuilds=" + collectedTestBuilds +
                '}';
    }
}
