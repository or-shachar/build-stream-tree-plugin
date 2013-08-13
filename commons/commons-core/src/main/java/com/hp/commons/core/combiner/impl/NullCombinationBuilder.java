package com.hp.commons.core.combiner.impl;

import com.hp.commons.core.combiner.CombinationBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/28/12
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 *
 * when we wish to combine ({@link com.hp.commons.core.combiner.CombinerFactory}) several
 * instances but do not care about the result of each (or any) instance's function application,
 * we can return null.
 *
 * this is a Singleton implementation of that behavior.
 *
 */
public class NullCombinationBuilder<T> implements CombinationBuilder<T> {

    /**
     * Singleton implementation
     *
     * no type as a trick, because the type doesn't matter for this behavior, allows us to use
     * this one instance for multiple different types.
     */
    private static NullCombinationBuilder instance;

    /**
     * Singleton implementation
     */
    @SuppressWarnings("unchecked")
    public static <T> NullCombinationBuilder<T> getInstance() {

        if (instance == null) {
            instance = new NullCombinationBuilder();
        }

        //cast to a typed NullCombinationBuilder even though we have no type - it doesn't matter
        //in the implementation due to type erasure, and our class reacts correctly
        //to all and any different types - it just returns null
        return (NullCombinationBuilder<T>)instance;
    }

    /**
     * Singleton implementation
     */
    private NullCombinationBuilder() {}


    /**
     *
     * @param inst completely ignored.
     */
    @Override
    public void instance(T inst) {}

    /**
     *
     * @return null. always.
     */
    @Override
    public T combine() {
        return null;
    }
}
