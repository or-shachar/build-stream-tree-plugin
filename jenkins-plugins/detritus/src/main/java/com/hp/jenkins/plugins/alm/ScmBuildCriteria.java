package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.criteria.Criteria;
import hudson.model.AbstractBuild;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/12/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScmBuildCriteria implements Criteria<AbstractBuild> {

    /*
    * SINGLETON
    * */
    private ScmBuildCriteria() { }

    private static ScmBuildCriteria instance;

    public static ScmBuildCriteria getInstance() {

        if (instance == null) {

            instance = new ScmBuildCriteria();
        }

        return instance;
    }

    /*
    * functionality...
    * */

    @Override
    public boolean isSuccessful(AbstractBuild tested) {
        return tested.getProject().getScm() != null;
    }
}
