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
 *
 * @param <T> type of map keys
 * @param <V> type of map values
 */
public class MapKeyCriteria<T, V> implements Criteria<Map.Entry<T, V>> {

    /**
     * keyCriteria the criteria to test the keys of the map entries with
     */
    private Criteria<T> internalCriteria;

    /**
     * @param keyCriteria the criteria to test the keys of the map entries with
     */
    public MapKeyCriteria(Criteria<T> keyCriteria) {
        this.internalCriteria = keyCriteria;
    }

    /**
     *
     * @param tested the map entry whose key will be tested by the {@link #internalCriteria}
     * @return true iff the key passes the internal criteria
     */
    @Override
    public boolean isSuccessful(Map.Entry<T, V> tested) {
        return (tested != null) && internalCriteria.isSuccessful(tested.getKey());
    }
}
