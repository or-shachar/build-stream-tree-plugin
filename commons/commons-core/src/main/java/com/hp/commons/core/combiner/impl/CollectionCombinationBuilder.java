package com.hp.commons.core.combiner.impl;

import com.hp.commons.core.collection.factory.CollectionFactory;
import com.hp.commons.core.combiner.CombinationBuilder;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 */
public class CollectionCombinationBuilder<T> implements CombinationBuilder<Collection<T>> {

    private Collection<T> internalCollection;
    private CollectionFactory collectionFactory;

    public CollectionCombinationBuilder(CollectionFactory factory) {

        this.collectionFactory = factory;
        reset();
    }

    private void reset() {

        this.internalCollection = this.collectionFactory.newCollection();
    }

    @Override
    public void instance(Collection<T> inst) {

        this.internalCollection.addAll(inst);
    }

    @Override
    public Collection<T> combine() {


        Collection<T> ret = internalCollection;

        reset();

        return ret;
    }
}
