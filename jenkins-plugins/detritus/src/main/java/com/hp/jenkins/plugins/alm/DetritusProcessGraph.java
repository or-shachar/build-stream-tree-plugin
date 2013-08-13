package com.hp.jenkins.plugins.alm;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/12/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DetritusProcessGraph {

    Collection<DetritusBuildWrapper> getTestBuilds();

    Collection<DetritusBuildWrapper> getScmProjects();


    FailedTests getFailingTests();

    DetritusScmRevisionMapping getRevisionRanges();
}
