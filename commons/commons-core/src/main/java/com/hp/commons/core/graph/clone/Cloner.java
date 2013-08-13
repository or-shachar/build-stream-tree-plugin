package com.hp.commons.core.graph.clone;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 *
 * an object that knows how to create clones for elements of type T.
 * @param <T> the type of the elments this cloner knows how to clone.
 */
public interface Cloner<T> {

    /**
     *
     * note the concrete implementation can do whatever it wants on the clone -
     * it can be a proxy, a subclass, a shallow copy, the same instance...
     * anything goes.
     *
     * @param node the element instance that we would like to clone
     * @return a clone of node
     *
     */
    public T clone(T node);
}
