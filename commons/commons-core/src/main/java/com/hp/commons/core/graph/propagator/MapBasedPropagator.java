package com.hp.commons.core.graph.propagator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/4/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 *
 * propagator that returns results according to a map. meaning, it has an inner map
 * node->collection(nodes) where the value collection is the neighbors of the key, so it's actually
 * node->neighbors
 *
 * this is an Adapter for a map to be used in interfaces requiring a propagator.
 *
 */
public class MapBasedPropagator<T> implements Propagator<T> {

    /**
     * mapping of the form node->neighbors to use for propagating
     */
    private Map<T, Collection<T>> map;

    /**
     *
     * @param mapping of the form node->neighbors to use for propagating
     */
    public MapBasedPropagator(Map<T, Collection<T>> mapping) {
        this.map = mapping;
    }

    /**
     *
     * @param source the node whose neighbors are being determined
     * @return neighbors according to the values matching "source" in {@link #map}
     */
    @Override
    public Collection<T> propagate(T source) {
        return this.map.containsKey(source) ?
                new HashSet<T>(this.map.get(source)) :
                Collections.<T>emptyList();
    }

    /**
     *
     * {@link #map} getter
     *
     * @return {@link #map}
     */
    public final Map<T, Collection<T>> getMap() {
        return map;
    }

    /**
     *
     * {@link #map} setter
     *
     * @param map set {@link #map}
     */
    public void setMap(Map<T,Collection<T>> map) {
        this.map = map;
    }
}
