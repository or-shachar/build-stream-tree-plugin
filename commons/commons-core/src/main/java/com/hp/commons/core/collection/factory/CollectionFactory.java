package com.hp.commons.core.collection.factory;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 *
 * sometimes you need to dynamically create a collection, and you want the developer to be able to choose
 * the collection type.
 * in those cases you can pass concrete implementations of this class.
 *
 * TODO: create extended interface that accept all common constructor arguments
 * TODO: this class should be typed to return a specific collection type
 * e.g. CollectionFactory<C extends Collection> but then how to enforce typesafety concerning <T> on newCollection?
 */
public interface CollectionFactory {

    /**
     *
     * @param <T> the type of the returned collection
     * @return an empty collection of a certain specific implementation (like ArrayList) type <T>
     */
    public <T> Collection<T> newCollection();
}
