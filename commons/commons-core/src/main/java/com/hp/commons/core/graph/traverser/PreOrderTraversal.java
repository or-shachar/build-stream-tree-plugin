package com.hp.commons.core.graph.traverser;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 *
 * traversal that first processes the current node and then goes on to process the neighbor nodes.
 * this form of calculation allows including the results from all nodes on the path from the source
 * to the current node in the computation.
 *
 * @param <POC> the result of processing the combined pre-order previously calculated results
 */
public interface PreOrderTraversal<C, POC, R, T> extends Traversal<C, R, T> {

    /**
     *
     * @return the component that combines results from previously traversed nodes on the path, and
     * the current node's intermediate result.
     */
    public PreOrderTraversalResultsCombiner<POC, T, R> getPreOrderCombiner();

    /**
     *
     * @param combiner the component that combines results from previously traversed nodes on the path, and
     * the current node's intermediate result.
     */
    public void setPreOrderCombiner(PreOrderTraversalResultsCombiner<POC, T, R> combiner);

    public PreOrderPropagationDecider<T, POC> getPropagationDecider();
    public void setPropagationDecider(PreOrderPropagationDecider<T, POC> decider);
}
