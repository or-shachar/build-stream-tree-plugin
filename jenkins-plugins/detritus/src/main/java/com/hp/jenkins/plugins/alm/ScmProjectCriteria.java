package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.criteria.Criteria;
import hudson.model.AbstractProject;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/12/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScmProjectCriteria implements Criteria<AbstractProject> {

    /*
    * SINGLETON
    * */
    private ScmProjectCriteria() { }

    private static ScmProjectCriteria instance;

    public static ScmProjectCriteria getInstance() {

        if (instance == null) {

            instance = new ScmProjectCriteria();
        }

        return instance;
    }

    /*
    * functionality...
    * */

    @Override
    public boolean isSuccessful(AbstractProject tested) {
        return tested.getScm() != null;
    }
}
