package com.hp.commons.core.graph.clone;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 *
 * class for relinking a cloned subgraph.
 * in this graph package there's an underlying data structure, and we wrap it in
 * graph like behavior - for example with propagators which help us decide which node
 * is connected to which node.
 * but the propagators rely on the underlying data to propagate.
 * if we clone a subgraph - meaning we made copies of each node, then immediately
 * after the cloning - each clone references its "old" neighborhood, the original's neighborhood.
 * we must update the clone to use it's new cloned neighbours instead.
 * we can't do this in the propagator, it must be done on the underlying data structure.
 *
 * this class is responsible for updating (or relinking) the neighborhood links in
 * whatever fashion is required.
 *
 */
public interface Relinker<T> {

    /**
     *
     * @param original2cloned a map of originals -> clones, whose values will be
     *                        relinked (with their neighbors) according to the mapping
     *
     */
    public void relink(Map<T, T> original2cloned);

}
