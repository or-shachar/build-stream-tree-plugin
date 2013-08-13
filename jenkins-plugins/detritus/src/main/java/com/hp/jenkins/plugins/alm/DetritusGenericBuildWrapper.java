package com.hp.jenkins.plugins.alm;

import hudson.model.Run;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusGenericBuildWrapper implements DetritusBuildWrapper {

    private Run build;

    public DetritusGenericBuildWrapper(Run build) {

        this.build = build;
    }

    public Run getJobBuild() {

        return this.build;
    }

    @Override
    public String toString() {
        return "DetritusGenericBuildWrapper{" +
                "build=" + build +
                '}';
    }
}
