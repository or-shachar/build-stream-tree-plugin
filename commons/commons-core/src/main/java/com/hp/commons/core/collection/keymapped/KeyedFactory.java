package com.hp.commons.core.collection.keymapped;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 *
 * represents something that can exchange tokens of type K for values of type V.
 * {@link com.hp.commons.core.collection.keymapped.KeyMappedSet} is a good example.
 *
 * different than {@link com.hp.commons.core.unique.KeyFactory} in that it gives elements according
 * to their tokens, and doesn't generate tokens.
 *
 */
public interface KeyedFactory<K,V> {

    /**
     *
     * depending on implementation this function may return newly allocated instances each time,
     * or reuse the same elements. it can do calculations, or use a cache, or whatever.
     * the contract is not strict.
     *
     * @param key the token whose associated value we wish to retrieve
     * @return the value association with token "key", depending on implementation.
     *
     */
    public V get(K key);
}
