package com.hp.commons.core.collection;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/9/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 *
 * this is an implementation for Map.Entry - which is sometimes useful, for example: when you treat
 * a map as a collection of entries, for compatability with Collection based interfaces,
 * and you wish to modify the map values inside this collection.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class MapEntryImpl<K,V> implements Map.Entry<K,V> {

    private K key;
    private V value;

    public MapEntryImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapEntryImpl)) return false;

        MapEntryImpl mapEntry = (MapEntryImpl) o;

        if (key != null ? !key.equals(mapEntry.key) : mapEntry.key != null) return false;
        if (value != null ? !value.equals(mapEntry.value) : mapEntry.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MapEntryImpl{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
