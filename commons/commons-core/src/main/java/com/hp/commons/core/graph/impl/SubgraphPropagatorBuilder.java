package com.hp.commons.core.graph.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.criteria.IsTrueCriteria;
import com.hp.commons.core.criteria.MapValueCriteria;
import com.hp.commons.core.graph.impl.combiner.SelectCurrentPreOrderResultsCombiner;
import com.hp.commons.core.graph.impl.traversal.MultipleSourcesTraversalImpl;
import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;
import com.hp.commons.core.graph.impl.traversal.PreOrderDfsTraversal;
import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Builder;
import com.hp.commons.core.graph.propagator.MapBasedPropagator;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.Traversal;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/4/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 *
 * factory for propagator that is a sub-propagator (i.e. returns some of the edges), by traversing the graph
 * and using a predefined handler on the nodes. if at any point a "true" value is returned by the handler for some target
 * node - all the nodes on the path from the sources to that target node are collected into the propagator.
 *
 */
public class SubgraphPropagatorBuilder<T> implements Builder<Propagator<T>> {

    /**
     * original propagator that gives structure to the graph. we want a sub-propagator of it.
     */
    private Propagator<T> propagator;

    /**
     * function that returns true if the node and the path leading to it should be collected.
     */
    private Handler<Boolean, T> detectionHandler;

    /**
     * starting nodes of the iteration
     */
    private Collection<T> nodes;

    /**
     *
     * @param propagator original propagator that gives structure to the graph. we want a sub-propagator of it.
     * @param detectionHandler function that returns true if the node and the path leading to it should be collected.
     * @param nodes starting nodes of the traversal
     */
    public SubgraphPropagatorBuilder(
            Propagator<T> propagator,
            Handler<Boolean, T> detectionHandler,
            Collection<T> nodes) {

        this.propagator = propagator;
        this.detectionHandler = detectionHandler;
        this.nodes = nodes;
    }


    @Override
    public MapBasedPropagator<T> build() {

        //used to store the sub-propagation
        Map<T, Collection<T>> mapping = new HashMap<T, Collection<T>>();

        //create a traversal that collects the propagator inspired subgraph into mapping,
        // with the specified detectionHandler
        //TODO: this is untyped, fix it.
        Traversal propagationMapBuildingTraversal =
                new PreOrderDfsTraversal(
                        this.detectionHandler,
                        SelectCurrentPreOrderResultsCombiner.getInstance(),
                        AlwaysPropagateDecider.getInstance(),
                        this.propagator,
                        new PropagationMapBuildingCombiner(mapping));

        //support multiple starting nodes in a non repeating fashion, and apply
        final Traversal nonRepeatingTraversal =
                new NonRepeatingTraversal(propagationMapBuildingTraversal);

        new MultipleSourcesTraversalImpl(nonRepeatingTraversal).traverse(nodes);

        //wrap the collected subgraph map in a propagator as befitting abstract API.
        MapBasedPropagator<T> propagator = new MapBasedPropagator<T>(mapping);
        return propagator;
    }

    /**
     * Created by IntelliJ IDEA.
     * User: grunzwei
     * Date: 2/4/12
     * Time: 4:04 PM
     * To change this template use File | Settings | File Templates.
     *
     * backtracking combiner for building a propagator, effectively choosing a subgraph according to some criteria.
     *
     */
    private static class PropagationMapBuildingCombiner<T> implements BacktrackingPreOrderTraversalResultsCombiner<Boolean, T, Boolean> {

        /**
         * the internal map structure to collect the subgraph of interest.
         */
        private Map<T, Collection<T>> map;
        private boolean includeLeaf;

        /**
         *
         * @param mapping the internal map structure to collect the subgraph of interest.
         *
         */
        public PropagationMapBuildingCombiner(
                Map<T, Collection<T>> mapping) {

            this.map = mapping;
        }

        /**
         *
         * @param node the current node, for which we wish to calculate some result.
         * @param nodeResult result of current node, before combining with neighbors.
         * @param propagatedResults results of neighbors
         * @return true iff the result of a node or a transitive neighbor is true. this also means the node -
         * and all the nodes up to it in the path from source to node are collected.
         */
        @Override
        public Boolean combine(T node, Boolean nodeResult, Map<T, Boolean> propagatedResults) {

            final Set<Map.Entry<T,Boolean>> items = propagatedResults.entrySet();
            //TODO: generalize, instead of Boolean and IsTrueCriteria... do whatever and Criteria that accepts or rejects it.
            CollectionUtils.filter(items, new MapValueCriteria<T, Boolean>(IsTrueCriteria.getInstance()));
            if (!items.isEmpty()) {
                map.put(node,propagatedResults.keySet());
                return true;
            }

            if (nodeResult) {
                map.put(node, Collections.<T>emptySet());
                return true;
            }

            return false;
        }
    }
}
