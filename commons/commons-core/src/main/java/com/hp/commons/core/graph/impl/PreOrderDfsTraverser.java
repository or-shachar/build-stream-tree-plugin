package com.hp.commons.core.graph.impl;

import com.hp.commons.core.collection.MapEntryImpl;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;
import com.hp.commons.core.graph.traverser.PreOrderBacktrackingTraverser;
import com.hp.commons.core.graph.traverser.PreOrderPropagationDecider;
import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 *
 * the actual implementation of traversal strategy. this is pre-order and backtracking, in DFS fashion.
 */
public class PreOrderDfsTraverser implements PreOrderBacktrackingTraverser {

    @Override
    public <C, POC, R, T> C traverse(
            List<Map.Entry<T, POC>> ancestorResults,
            T node,
            Handler<R, T> handler,
            PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner,
            PreOrderPropagationDecider<T, POC> propagationDecider,
            Propagator<T> propagator,
            Handler<C, T> propagationHandler,
            BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner) {

        //first handle our node, we are pre-order after all...
        R nodeResult = handler.apply(node);

        //we let the pre-order combiner do its work
        final POC nodePreOrderCombinationResult =
                preOrderCombiner.combine(node, nodeResult, ancestorResults);

        //we add the result to the list of results in this dfs branch
        ancestorResults.add(new MapEntryImpl(node, nodePreOrderCombinationResult));

        //if we should propagate, get the neighbours, otherwise - no edges
        final Collection<T> neighborhood =
                propagationDecider.propagate(ancestorResults) ?
                    propagator.propagate(node) :
                    (Collection<T>)Collections.emptySet();

        Map<T, C> results = new HashMap<T, C>(neighborhood.size());

        for (T neighbour: neighborhood) {

            final C propagatedResult = propagationHandler.apply(neighbour);

            results.put(neighbour, propagatedResult);
        }

        final C combinedResults = backtrackingCombiner.combine(node, nodePreOrderCombinationResult, results);

        //now that our iteration is done we must remove this node from
        //the list of results in this dfs branch
        ancestorResults.remove(ancestorResults.size() - 1);

        return combinedResults;
    }
}
