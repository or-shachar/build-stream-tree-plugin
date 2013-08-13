package com.hp.commons.core.handler.collector;

import com.hp.commons.core.handler.Handler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 *
 * a handler that transforms elements from E to T, and then adds the result to a collection
 *
 * @param <T> the type of the element after processing by the handler, and the type of the collection
 * @param <E> the type of the element being handled, and the handler input type.
 */
public class TransformingCollector<T, E> implements Collector<T,E> {

    /**
     * the function to apply onto the element before adding it to the collection
     */
    private Handler<T, E> handler;

    /**
     * the collection where the transformed elements should be stored
     */
    private Collection<T> collection;

    /**
     *
     * @param handler the function to apply onto the element before adding it to the collection
     */
    public TransformingCollector(Handler<T,E> handler) {

        this(handler, new ArrayList<T>());
    }

    /**
     *
     * @param handler the function to apply onto the element before adding it to the collection
     * @param collection the collection in which to store the transformed elements
     */
    public TransformingCollector(Handler<T,E> handler, Collection<T> collection) {

        this.handler = handler;
        this.collection = collection;
    }

    @Override
    public Collection<T> getCollection() {

        return collection;
    }

    @Override
    public void setCollection(Collection<T> collection) {

        this.collection = collection;
    }

    @Override
    public Void apply(E node) {

        final T transformed = this.handler.apply(node);

        if (transformed != null) {

            this.collection.add(transformed);
        }

        return null;
    }
}
