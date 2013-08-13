package com.hp.commons.core.graph.impl;

import com.hp.commons.core.graph.impl.combiner.SelectCurrentBacktrackingResultsCombiner;
import com.hp.commons.core.graph.impl.combiner.SelectCurrentPreOrderResultsCombiner;
import com.hp.commons.core.graph.impl.traversal.PreOrderDfsTraversal;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.Traversal;
import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO rename to TraversalUtils?
 */
public class TraversalFactory {

    //this is a static utility class.
    private TraversalFactory() {}

    /**
     *
     * @param nodeHandler function to apply on the nodes. its return value is the return value of the traversal.
     * @param propagator determines the neighbors to visit during the traversal.
     * @param <R> the traversal return type, and the node handler return type
     * @param <T> the node types
     * @return the simplest traversal: dfs with node-handler and propagator and nothing else.
     */
    public static <R,T> Traversal<R,R,T> getTraversal(
            Handler<R, T> nodeHandler,
            Propagator<T> propagator) {

        final Traversal<R,R,T> traversal =
                new PreOrderDfsTraversal<R,Object,R,T>(
                    nodeHandler,
                    SelectCurrentPreOrderResultsCombiner.getInstance(),
                    AlwaysPropagateDecider.getInstance(),
                    propagator,
                    SelectCurrentBacktrackingResultsCombiner.getInstance());

        return traversal;
    }

    /**
     *
     * @param nodeHandler function to apply on the nodes. its return value is the return value of the traversal.
     * @param propagator determines the neighbors to visit during the traversal.
     * @param preOrderCombiner component that combines current node handler result with that of previous nodes on the
     *                         path from the source to here. due to pre-order passage.
     * @param <POC> the return type of preOrderCombiner
     * @param <R> the traversal return type, and the node handler return type
     * @param <T> the node types
     * @return a pre-order configurable DFS traversal, using the node handler, propagator and pre-order-combiner defined.
     */
    public static <POC, R, T> Traversal<POC, R, T> getPreOrderTraversal(
            Handler<R, T> nodeHandler,
            Propagator<T> propagator,
            PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner) {

        final Traversal<POC, R, T> traversal =
                new PreOrderDfsTraversal<POC, POC, R, T>(
                    nodeHandler,
                    preOrderCombiner,
                    AlwaysPropagateDecider.getInstance(),
                    propagator,
                    SelectCurrentBacktrackingResultsCombiner.getInstance());

        return traversal;
    }



}
