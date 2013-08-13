package com.hp.commons.core.collection.keymapped;

import com.hp.commons.core.unique.KeyFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 *
 * concrete implementation of KeyMappedSet that supports using a custom KeyFactory given during construction.
 *
 * @param <K> the type of the key token the collection will return when given a value to store
 * @param <V> the type of the values you want to store in the collection
 */
public class KeyMappedSetImpl<K, V> implements KeyMappedSet<K, V> {

    /**
     * the internal map structure from token->value
     */
    private HashMap<K, V> keyToValueMap;

    /**
     * to make sure we return the same token if we are asked to store the value again.
     * consistency requirement.
     * TODO maybe should be part of KeyFactory and say ... ConsistentKeyFactory?
     */
    private HashMap<V, K> valueToKeyMap;

    /**
     * module in charge of generating keys for values.
     */
    private KeyFactory<V, K> keyFactory;

    /**
     *
     * @param keyFactory creates keys for values when they are inserted.
     */
    public KeyMappedSetImpl(KeyFactory<V, K> keyFactory) {

        this.keyToValueMap = new HashMap<K, V>();
        this.valueToKeyMap = new HashMap<V, K>();
        this.keyFactory = keyFactory;
    }

    @Override
    public K insert(V value) {

        //perhaps this value was already previously stored? be consistent.
        K key = valueToKeyMap.get(value);

        //if it wasn't previously stored
        if (key == null) {

            //generate a key for it and update the maps
            key = this.keyFactory.getKey(value);
            this.keyToValueMap.put(key, value);
            this.valueToKeyMap.put(value, key);
        }

        return key;
    }

    @Override
    public V remove(K key) {

        final V value = this.keyToValueMap.remove(key);
        this.valueToKeyMap.remove(value);

        return value;
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(this.keyToValueMap.keySet());
    }

    @Override
    public Set<V> valueSet() {
        return Collections.unmodifiableSet(this.valueToKeyMap.keySet());
    }

    @Override
    public Set<Map.Entry<K, V>> entriesSet() {
        return Collections.unmodifiableSet(this.keyToValueMap.entrySet());
    }

    @Override
    public int size() {
        return this.keyToValueMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.keyToValueMap.isEmpty();
    }

    @Override
    public Map<K, V> asMap() {
        return Collections.unmodifiableMap(this.keyToValueMap);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.keyToValueMap.entrySet().iterator();
    }

    @Override
    public V get(K key) {
        return this.keyToValueMap.get(key);
    }
}
