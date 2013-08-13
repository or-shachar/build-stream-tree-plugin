package com.hp.commons.core.graph.traverser;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 *
 * an interface for traversing logic: given a node, and all strategy elements such
 * as {@link Propagator}, {@link Handler} - starts the traversal process.
 *
 * <br/>TODO too many combiners handlers and things.. should be simpler
 */
public interface PreOrderBacktrackingTraverser {

    /**
     * starts the traversal with the given parameters.
     *
     * @param ancestorResults       previously calculated intermediate results, possible due to the pre-order nature
     *                              of the traversal.
     * @param source                the currently traversed node.
     * @param handler               function wrapper that handles currently traversed node.
     * @param preOrderCombiner      combines current result with ancestor results
     * @param propagationDecider    decide if propagation should proceed.
     * @param propagator            function to calculate the neighbors of "source" param node.
     * @param propagationHandler    function to apply on each neighbor (usually a traversal, probably encompassing
     *                              this traverser)
     * @param backtrackingCombiner  merges the results returned from the neighbors during backtracking and the currently
     *                              processed node
     *
     * @param <C> result type of this node, and of each neighbor. input to backtracking combiner and output of this method.
     * @param <POC> result type of pre-order combiner and input of backtracking combiner for current node intermediate result.
     * @param <R> return type of handler for current node.
     * @param <T> node type
     * @return result of traversal on this node after taking into account pre-order and backtracking combiners.
     */
    public <C, POC, R, T> C traverse(
            List<Map.Entry<T, POC>> ancestorResults,
            T source,
            Handler<R, T> handler,
            PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner,
            PreOrderPropagationDecider<T, POC> propagationDecider,
            Propagator<T> propagator,
            Handler<C, T> propagationHandler,
            BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner);
}
