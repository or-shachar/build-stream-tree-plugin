package com.hp.commons.core.unique;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 *
 * abstraction for a key factory. generates keys for groups of elements.
 *
 * different than {@link com.hp.commons.core.collection.keymapped.KeyedFactory} in that it generates
 * keys, and doesn't returns elements by their keys.
 *
 * @param <T> type of elements to make key for
 * @param <K> type of key
 */
public interface KeyFactory<T, K> {

    /**
     *
     * @param elements group of elements for which a key should be dispensed
     * @return key for group of elements param.
     */
    public K getKey(T... elements);

    /**
     *
     * @param elements group of elements for which a key should be dispensed
     * @return key for group of elements param.
     */
    public K getKey(Iterable<T> elements);
}
