package com.hp.commons.core.collection.aggregator;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/13/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 *
 * this class is used when iterating a collection, to produce some sort of aggregation.
 * the implementation defines the actually produced aggregation.
 *
 * for example, one can aggregate a list of integers to their sum or product.
 *
 * @param <R> the aggregation result
 * @param <T> the aggregated collection type
 */
public interface Aggregator<R, T> {

    /**
     *
     * @param elements the container that's about to be iterated.
     *
     * use this function to initialize the aggregation process. for example, you might need to create
     * a "result" collection and you can set it's size according to elements.size() for performance reasons.
     */
    public void init(Collection<T> elements);

    /**
     *
     * @param element the currently iterated element that you should add to the aggregation.
     * if the aggregator implementation is to create a sum of integers, then an implementation for this
     * method would be sum += element;
     */
    public void aggregate(T element);

    /**
     *
     * @return the result of the aggregation
     */
    public R finish();
}
