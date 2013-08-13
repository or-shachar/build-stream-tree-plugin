package com.hp.commons.core.combiner;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class combines a group of T typed objects into a single T object.
 * the original purpose of this class is to combine the results of multiple function applications
 * while keeping the same interface. see {@link CombiningInvocationHandler}).
 *
 *
 * @param <T> the type of the instances to be combined, and the return type.
 *
 */
public interface CombinationBuilder<T> {

    /**
     *
     * @param inst the parameter to add to result combination
     */
    public void instance(T inst);

    /**
     *
     * @return the combined elements
     */
    public T combine();

}
