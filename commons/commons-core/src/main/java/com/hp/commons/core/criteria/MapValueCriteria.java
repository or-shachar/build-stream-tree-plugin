package com.hp.commons.core.criteria;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/31/12
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 *
 * used when you want to apply criteria logic on a map according to it's key values.
 * you specify an internal criteria that you want used on the key elements of the map.
 *
 * use on the elements contained in {@link Map#entrySet}
 */
public class MapValueCriteria<K,T> implements Criteria<Map.Entry<K, T>> {

    /**
     * internalCriteria the criteria to test the values of the map entries with
     */
    private Criteria<T> internalCriteria;

    /**
     * @param internalCriteria the criteria to test the values of the map entries with
     */
    public MapValueCriteria(Criteria<T> internalCriteria) {
        this.internalCriteria = internalCriteria;
    }

    /**
     *
     * @param tested the map entry whose value will be tested by the {@link #internalCriteria}
     * @return true iff the value passes the internal criteria
     */
    @Override
    public boolean isSuccessful(Map.Entry<K, T> tested) {

        return (tested != null) && internalCriteria.isSuccessful(tested.getValue());
    }
}
