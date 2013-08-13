package com.hp.commons.core.handler.collector;

import com.hp.commons.core.criteria.AlwaysTrueCriteria;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/25/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 *
 * collector that collects all visited items
 *
 */
public class CollectingHandler<T> extends NonTransformingCollector<T> {

    /**
     *
     * @param col the collection in which to store visited items
     */
    public CollectingHandler(Collection<T> col) {

        //use the always true criteria to filter - so never filter - so collect everything
        super(AlwaysTrueCriteria.<T>getInstance(), col);
    }

}
