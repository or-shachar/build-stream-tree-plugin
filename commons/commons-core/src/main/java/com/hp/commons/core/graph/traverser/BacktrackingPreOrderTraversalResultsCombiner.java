package com.hp.commons.core.graph.traverser;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 *
 * processes the current node and the result of its neighbors and returns a combined value.
 *
 * <br/>when we are pre-order we have intermediate results of nodes in traversal root from source to the
 * current node.
 * when we are backtracking - we have the results from the neighbors.
 *
 * <br/>TODO too many combiners and resulters and things.. should be simpler.
 *
 * @param <C> the combined result type i.e. after backtracking - with neighbors results.
 * @param <T> the type of the graph nodes
 * @param <POC> the intermediate result of the current node, in pre-order processing
 */
public interface BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> {

    /**
     *
     * @param node the current node, for which we wish to calculate some result.
     * @param nodeResult result of current node, before combining with neighbors.
     * @param propagatedResults results of neighbors
     * @return combined result of this node and neighbors.
     */
    public C combine(T node, POC nodeResult, Map<T, C> propagatedResults);
}
