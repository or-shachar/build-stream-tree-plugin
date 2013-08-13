package com.hp.commons.core.graph.impl.combiner;

import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 *
 * backtracking results combiner that ignores the results of the neighbors and returns the result as it was given
 * by previous combiners such as the node handler and preorder combiner.
 *
 * <br/>this class is a Singleton.
 *
 */
public class SelectCurrentBacktrackingResultsCombiner implements BacktrackingPreOrderTraversalResultsCombiner {

    /**
     *  Singleton
     *  it's ok that the instance is not type safe, the one instance can be used to fit any type
     */
    private static SelectCurrentBacktrackingResultsCombiner instance;

    /**
     *  Singleton
     *  TODO cast to a typesafe instance and use suppresswarnings
     */
    public static SelectCurrentBacktrackingResultsCombiner getInstance() {

        if (instance == null) {
            instance = new SelectCurrentBacktrackingResultsCombiner();
        }

        return instance;
    }

    /**
     *  Singleton
     */
    private SelectCurrentBacktrackingResultsCombiner() {}

    @Override
    public Object combine(Object node, Object nodeResult, Map propagatedResults) {

        return nodeResult;
    }
}
