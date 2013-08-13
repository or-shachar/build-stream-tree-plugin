package com.hp.commons.core.criteria;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/31/12
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 *
 * an abstraction over the concept of a criteria.
 * can be used, for example, for filtering, or grouping.
 * can also be used for example in any other test/segmentation/classification scenario
 *
 * @param <T> type of elements on which this criteria is applicable
 */
public interface Criteria<T> {

    /**
     *
     * the concrete implementations of the Criteria class are what give this function it's meaning.
     * an example is an InstanceOf criteria which can test if objects are instanceof some predefined class.
     *
     * @param tested the element under test who we'd like to know if passes the criteria
     * @return true iff the element passes the criteria.
     *
     */
    public boolean isSuccessful(T tested);
}
