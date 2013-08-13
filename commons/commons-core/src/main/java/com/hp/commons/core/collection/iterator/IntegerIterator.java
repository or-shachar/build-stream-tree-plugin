package com.hp.commons.core.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class iterates integers.
 * good for getting sequential ids for example.
 *
 * TODO: instead of working with next, work with current. allows easier testing of max.
 * TODO: extend by StrictIntegerIterator that throws NoMoreElements if max reached, or OperationNotSupported on remove()
 */
public class IntegerIterator implements Iterator<Integer> {

    /**
     * this memeber lets us know once we've returned MAX_INT, and therefore have no more elements.
     */
    private boolean noMoreIntegers = false;

    /**
     * element to return on first next() call. the state of the iterator.
     */
    private int next;

    public IntegerIterator() {
        this(Integer.MIN_VALUE);
    }

    /**
     *
     * @param next element to return on first (next) next() call.
     *             the state of the iterator
     */
    public IntegerIterator(int next) {
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return !noMoreIntegers;
    }

    @Override
    public Integer next() {

        if (noMoreIntegers) {
            throw new NoSuchElementException("have surpassed Integer.MAX_VALUE, no more integers available.");
        }

        if (next == Integer.MAX_VALUE) {
            noMoreIntegers = true;
        }

        return next++;
    }

    @Override
    public void remove() {}

    public void reset(int nextValue) {
        next = nextValue;
        this.noMoreIntegers = false;
    }
}
