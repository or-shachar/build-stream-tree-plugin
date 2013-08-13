package com.hp.commons.core.graph.impl.traversal;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;
import com.hp.commons.core.graph.traverser.PreOrderBacktrackingTraversal;
import com.hp.commons.core.graph.traverser.PreOrderBacktrackingTraverser;
import com.hp.commons.core.graph.traverser.PreOrderPropagationDecider;
import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 *
 * backtracking and pre-order, accepts components that are in charge of each
 * part of the traversal
 */
public class PreOrderBacktrackingTraversalImpl<C, POC, R, T> implements
        PreOrderBacktrackingTraversal<C, POC, R, T> {

    /**
     * function to use when encounter a node
     */
    protected Handler<R, T> nodeHandler;

    /**
     * combiner to use when processing the current node with previous nodes' results.
     */
    protected PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner;

    /**
     * element that decides if propagation should continue
     */
    protected PreOrderPropagationDecider<T, POC> propagationDecider;

    /**
     * element that finds the neighbors of currently processed nodes
     */
    protected Propagator<T> propagator;

    /**
     * function to apply on each neighbor in order to continue the traversal.
     * usually the same traversal.
     */
    protected Handler<C, T> propagationHandler;

    /**
     * combiner to use for combining the results of the neighbor nodes with the current intermediate result composed
     * of this node and previously iterated pre-order intermediate results.
     */
    protected BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner;

    /**
     * traversal strategy - implementation of one step of the traversal
     */
    protected PreOrderBacktrackingTraverser traverser;

    /**
     *
     * @param nodeHandler function to use when encounter a node
     * @param preOrderCombiner combiner to use when processing the current node with previous nodes' results
     * @param propagationDecider element that decides if propagation should continue
     * @param propagator element that finds the neighbors of currently processed nodes
     * @param propagationHandler function to apply on each neighbor in order to continue the traversal.
     * usually the same traversal.
     * @param backtrackingCombiner  combiner to use for combining the results of the neighbor nodes with the current intermediate result composed
     * of this node and previously iterated pre-order intermediate results.
     * @param traverser traversal strategy - implementation of one step of the traversal
     */
    public PreOrderBacktrackingTraversalImpl(
            Handler<R, T> nodeHandler,
            PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner,
            PreOrderPropagationDecider<T, POC> propagationDecider,
            Propagator<T> propagator,
            Handler<C, T> propagationHandler,
            BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner,
            PreOrderBacktrackingTraverser traverser) {

        this.nodeHandler = nodeHandler;
        this.preOrderCombiner = preOrderCombiner;
        this.propagationDecider = propagationDecider;
        this.propagator = propagator;
        this.propagationHandler = propagationHandler;
        this.backtrackingCombiner = backtrackingCombiner;
        this.traverser = traverser;
    }

    public Handler<R, T> getNodeHandler() {
        return nodeHandler;
    }

    public void setNodeHandler(Handler<R, T> handler) {
        this.nodeHandler = handler;
    }

    public PreOrderTraversalResultsCombiner<POC, T, R> getPreOrderCombiner() {
        return preOrderCombiner;
    }

    public void setPreOrderCombiner(PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner) {
        this.preOrderCombiner = preOrderCombiner;
    }

    public PreOrderPropagationDecider<T, POC> getPropagationDecider() {
        return propagationDecider;
    }

    public void setPropagationDecider(PreOrderPropagationDecider<T, POC> propagationDecider) {
        this.propagationDecider = propagationDecider;
    }

    public Propagator<T> getPropagator() {
        return propagator;
    }

    public void setPropagator(Propagator<T> propagator) {
        this.propagator = propagator;
    }

    public Handler<C, T> getPropagationHandler() {
        return propagationHandler;
    }

    public void setPropagationHandler(Handler<C, T> propagationHandler) {
        this.propagationHandler = propagationHandler;
    }

    public BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> getBacktrackingResultsCombiner() {
        return backtrackingCombiner;
    }

    public void setBacktrackingResultsCombiner(BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner) {
        this.backtrackingCombiner = backtrackingCombiner;
    }

    @Override
    public PreOrderBacktrackingTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(PreOrderBacktrackingTraverser traverser) {
        this.traverser = traverser;
    }

    @Override
    public C apply(T node) {
        return this.traverser.<C, POC, R, T>traverse(
                new ArrayList<Map.Entry<T, POC>>(),
                node,
                this.nodeHandler,
                this.preOrderCombiner,
                this.propagationDecider,
                this.propagator,
                this.propagationHandler,
                this.backtrackingCombiner);
    }

    @Override
    public Object clone() {

        Object ret = null;

        try {
            ret =  super.clone();
        }
        catch (CloneNotSupportedException e) { /*impossible*/ }

        return ret;
    }
}
