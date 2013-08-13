package com.hp.commons.core.combiner.impl;

import com.hp.commons.core.combiner.CombinationBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/16/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class collects booleans, and returns true if they're all true.
 * it's intended to be used as a CombinationBuilder to combine interfaces with
 * functions that return only Boolean values, and indicating if they all succeeded or not.
 *
 */
public class CriteriaResultAllSuccessfulCombiner implements CombinationBuilder<Boolean> {

    /**
     * state of aggregation so far.
     */
    boolean aggregated = true;

    /**
     *
     * @param inst the parameter to add to result combination
     * if inst is false in any invocation, {@link #combine()} will return false
     */
    @Override
    public void instance(Boolean inst) {
        aggregated &= inst;
    }

    /**
     *
     * @return true iff all combined elements were true.
     */
    @Override
    public Boolean combine() {
        return aggregated;
    }
}
