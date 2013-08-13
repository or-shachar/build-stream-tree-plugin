package com.hp.commons.core.graph.propagator;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/4/12
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO rename to AbstractFactory and place in separate package, inside designpatterns package
 *
 * TODO BuildOrder visitable with the type that should be built, and Builder visitor that calls the correct contained
 * builder as needed... the method should be something like <BR extends BuildResult<BO extends BuildOrder>> BR build()
 */
public interface Builder<T> {

    //TODO rename to "create", according to abstract factory convention
    public T build();
}
