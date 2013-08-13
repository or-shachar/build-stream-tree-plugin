package com.hp.commons.core.graph.traverser;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;


/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 *
 * class that encapsulates all the decisions and behavior strategies required to
 * traverse a graph.
 *
 * @param <T> the elements this traversal traverses.
 * @param <R> the type of return values of the node handler function.
 * @param <C> the traversal result value type, the combined type of current node with previous nodes.
 *
 */
public interface Traversal<C, R, T> extends Cloneable, Handler<C, T> {


    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Basic Traversal Componenets - all Traversals have these
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     *
     * @return the handler object used on each visited node
     */
    public Handler<R, T> getNodeHandler();

    /**
     *
     * @param handler the handler object used on each visited node
     */
    public void setNodeHandler(Handler<R, T> handler);

    /**
     *
     * @return the propagator object used on each node to determine the neighborhood
     */
    public Propagator<T> getPropagator();

    /**
     *
     * @param propagator the propagator object used on each node to determine the neighborhood
     */
    public void setPropagator(Propagator<T> propagator);

    /**
     *
      * @return the handler to use on each of the neighbor elements
     * TODO design issue here, different handler for current node and neighbour node? TOO many result combiners and handlers... should be simpler
     *
     */
    public Handler<C, T> getPropagationHandler();

    /**
     *
     * @param handler to use on each of the neighbor elements
     */
    public void setPropagationHandler(Handler<C, T> handler);

    /**
    * TODO: some design issues here.. the traverser is always a backtraving traverser -
    *           so this should be a backtracking traversal? not much of a strategy design pattern...
    *
    * @return traversing strategy
    */
    public PreOrderBacktrackingTraverser getTraverser();

    /**
     *
     * @param traverser traversing strategy to use
     */
    public void setTraverser(PreOrderBacktrackingTraverser traverser);

    /**
     *
     * required in order to manipulate traversals
     * objects, for example to make them non-repeating, or multi-sourced.
     * TODO find some other way instead of implementing clone - we don't need deep copy, or at least separate into a different interface AugmentableTraversal..
     *
     * @return a clone of this traversal.
     * @see NonRepeatingTraversal
     */
    public Object clone();
}
