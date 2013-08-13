package com.hp.commons.core.unique;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/9/12
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 *
 * abstraction for something that is identifiable, where the identifier is generically typed.
 *
 * @param <I> the type of the identifier
 */
public interface Identifiable<I> {

    /**
     *
     * @return this identifiable objects identifier
     */
    public I getId();
}
