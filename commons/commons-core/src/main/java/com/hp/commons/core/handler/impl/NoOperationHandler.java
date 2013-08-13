package com.hp.commons.core.handler.impl;

import com.hp.commons.core.handler.Handler;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/25/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 *
 * a {@link Handler} function abstraction that does nothing.
 * this is useful when you need to comply to an interface that requires a handler, but don't
 * actually want to do anything.
 *
 * this class is a singleton.
 *
 * @param <T> type of the input this handler should ignore :)
 */
public class NoOperationHandler<T> implements Handler<T, T> {

    /**
     * singleton pattern
     * no type because it doesn't matter, we use a trick to return the same untyped
     * instance everywhere, and cast it appropriately to the type being requested
     */
    private static NoOperationHandler instance;

    /**
     * singleton pattern
     */
    private NoOperationHandler() {}

    /**
     * singleton pattern
     */
    //it's ok, this is a trick to return a single instance for multiple different usages.
    @SuppressWarnings("unchecked")
    public static <T> NoOperationHandler<T> getInstance() {

        if (instance == null) {
            instance = new NoOperationHandler();
        }

        return (NoOperationHandler<T>)instance;
    }

    /**
     *
     * does nothing!
     *
     * @param node the input value to ignore
     * @return the same node
     */
    @Override
    public T apply(T node) {

        return node;
    }
}
