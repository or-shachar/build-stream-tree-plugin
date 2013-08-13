package com.hp.jenkins.plugins.alm;

import hudson.model.Run;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DetritusBuildWrapper {

    Run getJobBuild();
}
