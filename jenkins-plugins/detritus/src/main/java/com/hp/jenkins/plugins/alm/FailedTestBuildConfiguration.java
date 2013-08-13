package com.hp.jenkins.plugins.alm;

import hudson.tasks.test.AbstractTestResultAction;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/13/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class FailedTestBuildConfiguration {

    private AbstractTestResultAction testResults;
    private DetritusBuildWrapper build;

    public FailedTestBuildConfiguration(DetritusBuildWrapper build, AbstractTestResultAction testResults) {
        this.build = build;
        this.testResults = testResults;
    }

    public AbstractTestResultAction getTestResults() {
        return testResults;
    }

    public DetritusBuildWrapper getBuild() {
        return build;
    }

    @Override
    public String toString() {
        return "FailedTestBuildConfiguration{" +
                "testResults=" + testResults +
                ", build=" + build +
                '}';
    }
}
