package com.hp.commons.core.graph.traverser;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 *
 * this class combines the result of the current node with the results from
 * previously calculated results.
 *
 * @param <T> the type of each visited node
 * @param <R> the result of processing each node on its own
 * @param <POC> the result of processing the combined pre-order previously calculated results
 *
 */
public interface PreOrderTraversalResultsCombiner<POC, T, R> {

    /**
     *
     * the concrete implementation should define how to combine with previous
     * results.
     *
     * @param node the currently visited element
     * @param currentResult the result of handling the node element on its own.
     *                      an ordered list, where each element is a node traversed in the path
     *                      from the traversal start to this node.
     *                      the first index belongs to the source element, and the last
     *                      index belongs to the previously traversed element en route
     *                      to here.
     * @param traversalResults the combined pre-order results of previously visited elements
     * @return the combined pre-order result of this element
     *
     */
    public POC combine(T node,
                       R currentResult,
                       List<Map.Entry<T, POC>> traversalResults);
}
