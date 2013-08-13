package com.hp.commons.core.handler.impl;

import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.handler.Handler;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 *
 * Adapter for {@link Criteria} to mask as {@link Handler}.
 * accepts a Criteria during initialization to apply later when given inputs.
 * if the input passes the criteria, the input is returned.
 * otherwise, null is returned.
 *
 *
 */
public class FilteringHandler<T> implements Handler<T, T> {

    /**
     * the criteria being wrapped
     */
    private Criteria<T> criteria;

    /**
     *
     * @param criteria the criteria being wrapped
     */
    public FilteringHandler(Criteria<T> criteria) {

        this.criteria = criteria;
    }

    /**
     *
     * @param node the input value
     * @return node if it passes the criteria, null otherwise.
     */
    @Override
    public T apply(T node) {

        return this.criteria.isSuccessful(node) ? node : null;
    }
}
