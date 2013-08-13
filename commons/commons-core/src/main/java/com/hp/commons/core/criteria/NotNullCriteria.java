package com.hp.commons.core.criteria;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/31/12
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 *
 * criteria for being different than null.
 * can be used to filter out nulls from a collection for example.
 *
 * this class is a singleton
 *
 */
public class NotNullCriteria implements Criteria<Object> {

    /**
     * singleton pattern implementation
     */
    private NotNullCriteria(){}

    /**
     * singleton pattern implementation
     */
    private static NotNullCriteria instance;

    /**
     * singleton pattern implementation
     */
    public static NotNullCriteria getInstance() {

        if (instance == null) {
            instance = new NotNullCriteria();
        }

        return instance;
    }

    /**
     *
     * @param tested the element under test who we'd like to know if passes the criteria
     * @return true iff not null
     */
    @Override
    public boolean isSuccessful(Object tested) {
        return tested != null;
    }
}
