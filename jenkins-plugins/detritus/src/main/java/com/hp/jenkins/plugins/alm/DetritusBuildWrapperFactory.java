package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.handler.Handler;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.Run;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.test.AbstractTestResultAction;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/12/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusBuildWrapperFactory implements Handler<DetritusBuildWrapper, Run> {

    private static DetritusBuildWrapperFactory instance = new DetritusBuildWrapperFactory();

    public static DetritusBuildWrapperFactory getInstance() {
        return instance;
    }

    @Override
    public DetritusBuildWrapper apply(Run runToWrap) {

        if (runToWrap == null) {
            return null;
        }

        if (runToWrap instanceof MatrixBuild) {

            //this is the build belonging to the matrix job, which is what is doing the triggering
            MatrixBuild matrixTriggeringBuild = (MatrixBuild)runToWrap;

            //these are the build of each of the internal configurations. we must filter out those with no errors.
            final List<MatrixRun> matrixConfigurationsBuilds = matrixTriggeringBuild.getExactRuns();
            Iterator<? extends Run> triggeringBuildsIter = matrixConfigurationsBuilds.iterator();

            while (triggeringBuildsIter.hasNext()) {

                Run matrixConfigBuild = triggeringBuildsIter.next();

                final AbstractTestResultAction testResultAction = matrixConfigBuild.getAction(AbstractTestResultAction.class);

                final List<CaseResult> failedTests = testResultAction.getFailedTests();

                if (failedTests.size() == 0) {

                    DetritusListener.getInstance().getLogger().println("no failing tests detected, removing configuration '" +
                            matrixConfigBuild + "'.");

                    triggeringBuildsIter.remove();
                }
            }

            if (!matrixConfigurationsBuilds.isEmpty()) {

                return new DetritusMatrixBuildWrapper(matrixTriggeringBuild, matrixConfigurationsBuilds);
            }

            return null;
        }

        else {

            return new DetritusGenericBuildWrapper(runToWrap);
        }
    }
}
