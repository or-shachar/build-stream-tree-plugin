package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.criteria.Criteria;
import hudson.model.AbstractBuild;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBuildCriteria implements Criteria<AbstractBuild> {


    private TestBuildCriteria() {}

    private static Criteria<AbstractBuild> instance;

    public static Criteria<AbstractBuild> getInstance() {

        if (instance == null) {

            instance = new TestBuildCriteria();
        }

        return instance;
    }

    @Override
    public boolean isSuccessful(AbstractBuild tested) {
        return tested.getTestResultAction() != null;
    }
}
