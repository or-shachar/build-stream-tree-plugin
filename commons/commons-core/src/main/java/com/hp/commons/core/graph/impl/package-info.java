/**
 *
 * contains common implementations of the graph abstraction package {@link com.hp.commons.core.graph}
 *
 * TODO the over-abundance of implementations and multiple interfaces shows that the design is bad.
 * names are misleading, strategies that are supposed to be flexible are not.
 * can't easily create a BFS style post-order traversal for example.
 *
 * new design suggestion: traversal can have a state that is kept for current step. the different componenets
 * have methods matching events such as beforeCurrentNodeProcessing to which we pass the state element.
 * the components can access the state, and retrieve whatever information they need.
 * for example, the resultCombiner can listen to beforeCurrentNodeProcessing(previousResults, state) to retrieve pre-order if it cares
 * about pre-order,  currentNodeProcessing(node, result, state) and afterNeighborProcessing(neighborResults, state)
 * the traverser then executes the combiner's combine() method which can return data according to what it has collected...
 *
 * something like that anyway.
 */
package com.hp.commons.core.graph.impl;