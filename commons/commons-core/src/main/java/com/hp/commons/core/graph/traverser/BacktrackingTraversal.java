package com.hp.commons.core.graph.traverser;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 *
 * backtracking traversals have an element that can combine the current node and the results
 * from the neighbors into a bigger-picture result.
 *
 * <br/>TODO: too many results and combiners and stuff... should be simpler
 *
 * @see BacktrackingPreOrderTraversalResultsCombiner
 *
 * @param <POC> the intermediate result type of the current node, in pre-order processing
 */
public interface BacktrackingTraversal<C, POC, R, T> extends Traversal<C, R, T> {

    public BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> getBacktrackingResultsCombiner();
    public void setBacktrackingResultsCombiner(BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> combiner);
}
