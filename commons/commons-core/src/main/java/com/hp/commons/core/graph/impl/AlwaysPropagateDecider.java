package com.hp.commons.core.graph.impl;

import com.hp.commons.core.graph.traverser.PreOrderPropagationDecider;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO this class is totally redundant, remove it
 *
 */
@Deprecated
public class AlwaysPropagateDecider implements PreOrderPropagationDecider {


    private static PreOrderPropagationDecider instance;

    public static PreOrderPropagationDecider getInstance() {

        if (instance == null){
            instance = new AlwaysPropagateDecider();
        }

        return instance;
    }

    @Override
    public boolean propagate(List traversalResults) {
        return true;
    }
}
