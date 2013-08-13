package com.hp.commons.core.collection.aggregator;

import com.hp.commons.core.collection.MapEntryImpl;
import com.hp.commons.core.handler.Handler;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/13/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 *
 * aggregator calculating the max element of a collection after a transformation has been applied
 * to make the element comparable.
 *
 * usually it would be enough to map the elements to comparables, and then use Collection.max() -
 * but then you'll get the max comparable and not the original element, which sometimes complicates things.
 * also, you'll need to iterate the collection twice, and also store the entire comparable collection -
 * that's a performance issue.
 *
 * so you could instead just implement a comparator and use collection.max, but sometimes it's simpler
 * to use a class' pre-existing comparator or "natural ordering".
 * for example, if your element can easily be transformed to an Integer.
 *
 * @param <C> the result - largest comparable in the aggregated collection
 * @param <T> the type of the collection being aggregated
 */
public class MaxAggregator<C extends Comparable, T> implements Aggregator<Map.Entry<T, C>, T> {

    /**
     * the function that turns the aggregated collection elements to comparables.
     */
    private Handler<C, T> comparifier;

    /**
     * the result's computed comparable
     */
    private C maximumComparable;

    /**
     * the result
     */
    private T maximum;


    /**
     *
     * @param comparifier the function that turns the aggregated collection elements to comparables.
     *                    it is assumed that it does not return null.
     */
    public MaxAggregator(Handler<C, T> comparifier) {
        this.comparifier = comparifier;
    }

    @Override
    public void init(Collection<T> elements) {

        maximum = null;
        maximumComparable = null;
    }

    @Override
    public void aggregate(T element) {

        final C comparable = comparifier.apply(element);

        if (maximumComparable == null || (comparable.compareTo(maximumComparable) > 0)) {
            maximum = element;
            maximumComparable = comparable;
        }
    }

    /**
     *
     * @return null if no aggregated collection was empty, or the largest comparable otherwise.
     */
    @Override
    public Map.Entry<T, C> finish() {

        return (maximum == null) ||
                (maximumComparable == null) ?
                null :
                new MapEntryImpl<T, C>(maximum, maximumComparable);
    }
}
