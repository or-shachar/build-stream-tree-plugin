package com.hp.commons.core.graph.traverser;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 *
 * sometimes we want to traverse a graph not from one node, but from several.
 * for example, when the graph is not strongly connected, such as a forest.
 *
 * at other times there might be some performance advantage and the implementation of the traverser
 * could benefit from starting at several specific points.
 * an example of this could be a multi-threaded traversal that can start at different points of the graph
 * on multiple threads.
 *
 */
public interface MultipleSourcesTraversal<R, T> {

    /**
     *
     * @param sources to start the traversal from.
     * @return mapping of "sources" nodes and their corresponding traversal results.
     */
    public Map<T, R> traverse(Collection<T> sources);
}
