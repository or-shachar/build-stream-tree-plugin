package com.hp.commons.core.graph.impl.traversal;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.PreOrderBacktrackingTraverser;
import com.hp.commons.core.graph.traverser.Traversal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 *
 * this traversal will not process nodes that have already been processed in the past.
 * can be very useful with {@link MultipleSourcesTraversalImpl}.
 *
 * <br/>this traversal will cause any graph to be traversed as if a tree - visiting all nodes, but not all edges.
 *
 * <br/>with slight modification this can be useful for multithreaded traversing, if the collection of visited nodes is
 * shared and synced.
 *
 */
public class NonRepeatingTraversal<C, R, T> implements Traversal<C, R, T>, Propagator<T> {

    /**
     * map of previously visited nodes -> results of those nodes
     */
    private Map<T, C> visitedNodes;

    /**
     * the actual traversal logic we wish to use, and augment making it non-repeating.
     */
    private Traversal<C, R, T> internalTraversal;

    /**
     * actual handler logic that we augment by making sure it is not applied twice on the same node.
     */
    private Handler<C, T> wrappedPropagationHandler;

    /**
     * actual propagation logic that we augment by removing neighbors we've already visisted.
     */
    private Propagator<T> wrappedPropagator;

    /**
     *
     * @param traversal to wrap and make non repeating
     */
    public NonRepeatingTraversal(Traversal<C, R, T> traversal) {

        this.visitedNodes = new HashMap<T, C>();

        //we're going to need to replace the handler and propagator, and we don't want to modify the input instance
        //so we clone() instead. ok because Traversal extends Cloneable.
        //TODO make it so Traversable won't have extend Cloneable...?
        this.internalTraversal = (Traversal)traversal.clone();

        wrappedPropagationHandler = this.internalTraversal.getPropagationHandler();
        wrappedPropagator = this.internalTraversal.getPropagator();

        this.internalTraversal.setPropagationHandler(this);
        this.internalTraversal.setPropagator(this);
    }

    @Override
    public C apply(T node) {

        if (!this.visitedNodes.containsKey(node)) {

            //mark the node, so that when propagating we won't iterate it again
            this.visitedNodes.put(node, null);

            final C applicationResult = wrappedPropagationHandler.apply(node);
            this.visitedNodes.put(node, applicationResult);
        }

        return this.visitedNodes.get(node);
    }

    @Override
    public Collection<T> propagate(T source) {

        final Collection<T> propagation = wrappedPropagator.propagate(source);
        propagation.removeAll(this.visitedNodes.keySet());

        return propagation;
    }

    @Override
    //TODO: the wrapped objects and visited nodes should be cloned as well....
    public Object clone() {

        Object ret = null;

        try {
            ret =  super.clone();
        }
        catch (CloneNotSupportedException e) { /* impossible */ }

        return ret;
    }

    @Override
    public Handler<R, T> getNodeHandler() {
        return internalTraversal.getNodeHandler();
    }

    @Override
    public void setNodeHandler(Handler<R, T> rtHandler) {
        internalTraversal.setNodeHandler(rtHandler);
    }

    @Override
    public Propagator<T> getPropagator() {
        return wrappedPropagator;
    }

    @Override
    public void setPropagator(Propagator<T> tPropagator) {
        wrappedPropagator = tPropagator;
    }

    @Override
    public Handler<C, T> getPropagationHandler() {
        return wrappedPropagationHandler;
    }

    @Override
    public void setPropagationHandler(Handler<C, T> rtHandler) {
        wrappedPropagationHandler = rtHandler;
    }

    @Override
    public PreOrderBacktrackingTraverser getTraverser() {
        return internalTraversal.getTraverser();
    }

    @Override
    public void setTraverser(PreOrderBacktrackingTraverser traverser) {
        internalTraversal.setTraverser(traverser);
    }
}
