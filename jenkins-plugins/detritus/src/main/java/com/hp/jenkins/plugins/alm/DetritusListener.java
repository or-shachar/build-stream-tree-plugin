package com.hp.jenkins.plugins.alm;

import hudson.model.BuildListener;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DetritusListener {

    private static BuildListener wrapped;

    public static void setInstance(BuildListener wrapped) {
        DetritusListener.wrapped = wrapped;
    }

    public static BuildListener getInstance() {
        return wrapped;
    }

}
