package com.hp.commons.core.graph.impl.combiner;

import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 *
 * a pre-order results combiner that ignores the previous results and returns the node handler's result.
 *
 * this class is a Singleton
 */
public class SelectCurrentPreOrderResultsCombiner implements PreOrderTraversalResultsCombiner {

    /**
     * Singleton
     */
    private SelectCurrentPreOrderResultsCombiner() {}

    /**
     * Singleton
     * this instance should be unparameteryzed on purpose as the one instance will work for any type
     */
    private static SelectCurrentPreOrderResultsCombiner instance;

    /**
     * Singleton
     *
     * TODO make typesafe: the type <R> should parameterize the returned value, a cast in implementation an suppresswarnings, this class should be typed
     */
    public static <R> SelectCurrentPreOrderResultsCombiner getInstance() {

        if (instance == null) {
            instance = new SelectCurrentPreOrderResultsCombiner();
        }

        return instance;
    }

    @Override
    public Object combine(Object node, Object currentResult, List traversalResults) {
        return currentResult;
    }
}
