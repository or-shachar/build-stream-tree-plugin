package com.hp.commons.core.handler.collector;

import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.handler.impl.FilteringHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO: the name is bad, should be CriteriaCollector - it collects by criteria...
 *
 * this class collects visited items if they pass a criteria.
 *
 */
public class NonTransformingCollector<T> extends TransformingCollector<T, T> {

    /**
     *
     * @param criteria that must be passed for visited item to be collected
     */
    public NonTransformingCollector(Criteria<T> criteria) {
        this(criteria, new ArrayList<T>());
    }

    /**
     *
     * @param criteria that must be passed for visited item to be collected
     * @param collection the collection to which the visited item will be added
     */
    public NonTransformingCollector(Criteria<T> criteria, Collection<T> collection) {

        /**
         * collector doesn't collect nulls, and filtering-handler returns nulls for items that
         * don't pass the criteria, and so we only collect items that pass the criteria
         */
        super(new FilteringHandler<T>(criteria), collection);
    }

}
