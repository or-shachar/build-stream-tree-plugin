package com.hp.commons.core.graph.impl.traversal;

import com.hp.commons.core.graph.impl.PreOrderDfsTraverser;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.BacktrackingPreOrderTraversalResultsCombiner;
import com.hp.commons.core.graph.traverser.PreOrderPropagationDecider;
import com.hp.commons.core.graph.traverser.PreOrderTraversalResultsCombiner;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/4/12
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 *
 * most common implementation of a traversal. makes the next step of the traversal, be the same traversal.
 * the next step of the travesal is usually configurable and can be any function. this class hides that
 * configuration and uses the most common setting - same traversal behavior.
 */
public class PreOrderDfsTraversal<C, POC, R, T> extends PreOrderBacktrackingTraversalImpl<C, POC, R, T> {

    public PreOrderDfsTraversal(
            Handler<R, T> nodeHandler,
            PreOrderTraversalResultsCombiner<POC, T, R> preOrderCombiner,
            PreOrderPropagationDecider<T, POC> tpocPreOrderPropagationDecider,
            Propagator<T> tPropagator,
            BacktrackingPreOrderTraversalResultsCombiner<C, T, POC> backtrackingCombiner) {

        super (nodeHandler,
                preOrderCombiner,
                tpocPreOrderPropagationDecider,
                tPropagator,
                null,
                backtrackingCombiner,
                new PreOrderDfsTraverser());

        this.setPropagationHandler(this);
    }

    @Override
    public Object clone() {

        PreOrderDfsTraversal ret = (PreOrderDfsTraversal)super.clone();

        //update the older reference, still linking to the previous copy
        ret.setPropagationHandler(ret);

        return ret;
    }
}
