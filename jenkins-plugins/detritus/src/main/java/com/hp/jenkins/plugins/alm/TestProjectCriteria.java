package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.criteria.Criteria;
import hudson.model.AbstractProject;
import hudson.tasks.test.TestResultProjectAction;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestProjectCriteria implements Criteria<AbstractProject> {


    private TestProjectCriteria() {}

    private static Criteria<AbstractProject> instance;

    public static Criteria<AbstractProject> getInstance() {

        if (instance == null) {

            instance = new TestProjectCriteria();
        }

        return instance;
    }

    @Override
    public boolean isSuccessful(AbstractProject tested) {

        return tested.getAction(TestResultProjectAction.class) != null;
    }
}
