package com.hp.jenkins.plugins.alm;

import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.Run;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusMatrixBuildWrapper implements DetritusBuildWrapper {

    private MatrixBuild matrixBuild;
    private List<MatrixRun> configurationBuilds;

    public DetritusMatrixBuildWrapper(MatrixBuild matrixTriggeringBuild, List<MatrixRun> matrixConfigurationsBuilds) {
        this.matrixBuild = matrixTriggeringBuild;
        this.configurationBuilds = matrixConfigurationsBuilds;
    }

    public Run getJobBuild() {
        return matrixBuild;
    }

    @Override
    public String toString() {
        return "DetritusMatrixBuildWrapper{" +
                "matrixBuild=" + matrixBuild +
                ", configurationBuilds=" + configurationBuilds +
                '}';
    }
}
