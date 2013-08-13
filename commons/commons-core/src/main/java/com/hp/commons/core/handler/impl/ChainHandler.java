package com.hp.commons.core.handler.impl;

import com.hp.commons.core.handler.Handler;


/**
 * this class is a handler that delegates its logic to 2 sub-handlers, whose inputs and outputs
 * interlock - creating a chain link.
 * use of multiple such handlers hierarchically can create an actual chain.
 *
 * you can't type-safely create a real chain with more than 2 handlers, because when taking
 * an array there is no static way of making each element's type interlock with that of the previous handler.
 *
 * @param <RET> the chain link returne value type
 * @param <SND_IN> the input type of the second handler
 * @param <FST_OUT> the output type of the first handler and the input type of the second handler
 * @param <INPUT> the input type of the first handler
 */
public class ChainHandler<RET, SND_IN, FST_OUT extends SND_IN, INPUT> implements Handler<RET, INPUT> {

    /**
     * the second part of the chain, to apply on the results of the first part
     */
    private Handler<RET, SND_IN> second;

    /**
     * the first part of the chain, which will be further processed by the second part
     */
    private Handler<FST_OUT, INPUT> first;

    //TODO change the order, second second and first first
    //TODO handler "second" return extends RET, not RET itself?

    /**
     *
     * @param second the second part of the chain, to apply on the results of the first part
     * @param first the first part of the chain, which will be further processed by the second part
     */
    public ChainHandler(Handler<RET, SND_IN> second, Handler<FST_OUT, INPUT> first) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     * @param node the input value
     * @return second(first(node))
     */
    @Override
    public RET apply(INPUT node) {
        return second.apply(first.apply(node));
    }
}
