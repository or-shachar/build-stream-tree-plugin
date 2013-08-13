package com.hp.commons.core.collection.factory;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 *
 * factory that dynamically produces a HashSet implementation of a collection.
 */
public class HashSetFactory implements CollectionFactory {

    /**
     *
     * @param <T> the type of the hashset to return
     * @return new hashset instance of type <T>
     */
    @Override
    public <T> HashSet<T> newCollection() {
        return new HashSet<T>();
    }
}
