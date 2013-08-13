package com.hp.commons.core.graph.impl.combiner;

import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 *
 * <br/>backtracking pre order results combiner that returns null - for when you don't care about the return value but
 * still need to pass something to the API...
 *
 * <br/>this class is a singleton.
 *
 * TODO: usage of Object in generics is probably wrong here, wildcard needed?
 */
public class NullTraversalResultsCombiner<T> implements BacktrackingPreOrderTraversalResultsCombiner<Object, T, Object> {

    /**
     * Singleton
     */
    private NullTraversalResultsCombiner() {}

    /**
     * Singleton, intentionally not type safe as the same instance can be used for different types
     */
    private static NullTraversalResultsCombiner instance;

    /**
     * Singleton
     * TODO make this function typesafe by accepting <T> and using suppresswarnings, with cast of returned instance.
     */
    public static NullTraversalResultsCombiner getInstance() {

        if (instance == null) {
            instance = new NullTraversalResultsCombiner();
        }

        return instance;
    }

    @Override
    public Object combine(T node, Object nodeResult, Map<T, Object> propagatedResults) {
        return null;
    }
}
