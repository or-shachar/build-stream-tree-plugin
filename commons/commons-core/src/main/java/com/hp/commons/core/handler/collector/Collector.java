package com.hp.commons.core.handler.collector;

import com.hp.commons.core.handler.Handler;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class is a handler that will add elements to a collection when it handles them.
 *
 */
//TODO why not VoidHandler<E>?
public interface Collector<T, E> extends Handler<Void, E> {

    /**
     *
     * @return the collection where elements are stored into
     */
    public Collection<T> getCollection();

    /**
     *
     * @param collection the collection where elements should be stored in
     */
    public void setCollection(Collection<T> collection);

    /**
     *
     * adds the element to the collection.
     * exact behavior is defined by the concrete implementation.
     *
     * @param node the value to collect
     * @return return type is Void so nothing. only implementation of Void is null.
     */
    @Override
    public Void apply(E node);
}
