package com.hp.commons.core.unique;

import com.hp.commons.core.collection.iterator.IntegerIterator;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 *
 * key factory that returns inceasing integers as keys.
 * the class is not consistent, and will return for the same elements different values each time.
 *
 * @param <T> the type of the element groups for which keys should be dispensed
 */
public class SequentialIntegerKeysFactory<T> implements KeyFactory<T, Integer> {

    /**
     * internal iterator that produces the ever-increasing integers.
     */
    private IntegerIterator iter;

    /**
     *
     * @param start the value from which to start incrementing
     */
    public SequentialIntegerKeysFactory(int start) {

        iter = new IntegerIterator(start);
    }

    /**
     * sets the initial value to {@link Integer#MIN_VALUE}
     */
    public SequentialIntegerKeysFactory() {

        this(Integer.MIN_VALUE);
    }


    @Override
    public Integer getKey(T... elements) {
        return iter.next();
    }

    @Override
    public Integer getKey(Iterable<T> elements) {
        return iter.next();
    }
}
