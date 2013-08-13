package com.hp.commons.core.graph.traverser;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 *
 * traversal that is both backtracking and pre-order.
 * @see BacktrackingTraversal
 * @see PreOrderTraversal
 *
 * @param <C> the traversal result value type, the combined type of current node with previous nodes.
 * @param <POC> the intermediate result type of the current node, in pre-order processing
 * @param <R> the type of return values of the node handler function.
 * @param <T> the elements this traversal traverses.
 */
public interface PreOrderBacktrackingTraversal<C, POC, R, T> extends
        BacktrackingTraversal<C, POC, R, T>,
        PreOrderTraversal<C, POC, R, T> {
}
