package com.hp.commons.core.handler.impl;

import com.hp.commons.core.handler.Handler;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/16/12
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 *
 * handler that casts elements of type E to elements of type R.
 *
 * @param <E> the input type before the cast
 * @param <R> the output type after the cast
 */
public class CastingHandler<E, R extends E> implements Handler <R,E> {

    /**
     * the type to cast to
     */
    private Class<R> castTarget;

    /**
     *
     * @param castTarget the type to cast to
     */
    public CastingHandler(Class<R> castTarget) {
        this.castTarget = castTarget;
    }

    /**
     *
     * @param node the input value
     * @return node cast to R
     *
     * @throws ClassCastException if node cannot be cast to R
     */
    @Override
    public R apply(E node) {

        if (!castTarget.isAssignableFrom(node.getClass())) {

            throw new ClassCastException("failed to cast instance " + node + " of type '" +
                    node.getClass().getName() + "' to type '" +
                    castTarget.getName() + "'");
        }

        return (R)node;
    }
}
