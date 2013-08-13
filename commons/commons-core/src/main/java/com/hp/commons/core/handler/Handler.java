package com.hp.commons.core.handler;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/29/12
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO reflection handler that accepts in constructor the name of a method/Method and activates it on the node -
 * will make Handler equivalent to Function (jenkins) or Method or whatever...
 *
 * this class is a function on node of type <T> that returns a value of type <R>
 *
 * @param <T> the input parameter type
 * @param <R> the return value type
 */
public interface Handler<R, T> {

    /**
     *
     * @param node the input value
     * @return the concrete implementation of the Handler on input param "node"
     */
    public R apply(T node);
}
