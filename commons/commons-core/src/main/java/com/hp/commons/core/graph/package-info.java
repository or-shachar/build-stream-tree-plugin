
/**
 * this package contains means of regarding any data type as a graph, for the purpose of traversal
 *
 * the graph structure is given by a {@link Propagator} object,
 * which is intended to use the underlying data structure API to generate the propagated nodes, for a
 * node that is being traversed.
 *
 * {@link Handler} objects are intended to do whatever it is the user
 * intends to do with each traversed node.
 *
 * {@link Traversal} and {@link PreOrderBacktrackingTraverser}
 * objects are used to traverse the graph to obtain some kind of result.
 *
 * {@link PreOrderBacktrackingTraverser} defines the strategy of traversing the node,
 * while {@link Traversal} contains all the elements
 * required to actually traverse, and just needs a "start" node.
 *
 * different forms of traversal may require additional complexity, in the form of combiners to return
 * a value from the graph traversal. for example, pre-order traversals that first {@link Handler#apply}
 * the current node, and only then traverse the neighbours might wish to transfer the result of the node
 * to the neighbours. backtracking traversals that obtain results from each of the neighbours might
 * wish to use these values along with the value of the current node.
 * @see BacktrackingPreOrderTraversalResultsCombiner
 * @see PreOrderTraversalResultsCombiner
 *
 */
package com.hp.commons.core.graph;

import com.hp.commons.core.graph.traverser.*;
import com.hp.commons.core.handler.*;
import com.hp.commons.core.graph.propagator.*;