package com.hp.commons.core.criteria;


/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/6/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 *
 * a criteria that checks if the tested instance is an instance of a predefined superclass
 *
 * @param <T> elements on which this criteria is applicable
 */
public class InstanceOfCriteria<T> implements Criteria<T> {

    /**
     * the class who is potentially the super class of the element being tested
     */
    private Class<?> superClass;

    /**
     *
     * @param superClass the class who is potentially the super class of the element being tested
     */
    public InstanceOfCriteria(Class<?> superClass) {
        this.superClass = superClass;
    }

    /**
     *
     * @param tested the element under test who we'd like to know if passes the criteria
     * @return true if tested is an instance of {@link #superClass}
     */
    @Override
    public boolean isSuccessful(T tested) {

        return (tested == null) || superClass.isAssignableFrom(tested.getClass());
    }
}
