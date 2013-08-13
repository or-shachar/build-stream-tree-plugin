package com.hp.commons.core.collection.keymapped;


import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/8/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class let's you keep values with keys, but it supplies the key creation logic.
 * similar to a car-parking service or a coatroom where you give an item and get a token so you can retrieve
 * it later.
 *
 * good for serializations, stubs, simple id creation, caching..
 *
 * @param <K> the type of the key token the collection will return when given a value to store
 * @param <V> the type of the values you want to store in the collection
 */
public interface KeyMappedSet<K, V> extends Iterable<Map.Entry<K, V>>, KeyedFactory<K,V> {

    /**
     *
     * @param value the value you want to store
     * @return a key corresponding to the value for later retrieval
     */
    public K insert(V value);

    /**
     *
     * @param key the token for the item you want removed
     * @return the removed value matching the key token.
     */
    public V remove(K key);

    /**
     *
     * @return a collection of all keys/tokens
     */
    public Set<K> keySet();

    /**
     *
     * @return a collection of all values
     */
    public Set<V> valueSet();

    /**
     *
     * @return a collection of all token->value pairs
     */
    public Set<Map.Entry<K, V>> entriesSet();

    /**
     *
     * @return size of the collection
     */
    public int size();

    /**
     *
     * @return true iff the collection is empty (e.g. size == 0)
     */
    public boolean isEmpty();

    /**
     *
     * @return the internal map object
     */
    public Map<K,V> asMap();
}
