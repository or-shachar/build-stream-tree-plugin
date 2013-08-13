package com.hp.commons.core.combiner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO make an annonymous inner class of ProxyUtils futuristic combine method
 *
 * this class is used to generate a proxy that combines several instances sharing
 * the same interface into one instance with that interface, for compatability with
 * API that doesn't accept a collection.
 *
 * @see CombinerFactory
 */
public class CombiningInvocationHandler<T> implements InvocationHandler {

    /**
     * the collection of interface implementation that contain the logic we wish to apply
     */
    private Iterable<T> combinedInstances;

    /**
     * the strategy used to combine the results from each of {@link #combinedInstances}
     */
    private CombinationBuilder resultsCombiner;

    /**
     *
     * @param instances the collection of interface implementation that contain the logic we wish to apply
     * @param resultsCombiner the strategy used to combine the results from each of combinedInstances
     */
    public CombiningInvocationHandler(
            Iterable<T> instances,
            CombinationBuilder resultsCombiner) {

        this.combinedInstances = instances;
        this.resultsCombiner = resultsCombiner;
    }

    /**
     *
     * @param proxy ignored
     * @param method the method being invoked on each of {@link #combinedInstances}
     * @param args the args to the method
     * @return combined result returned by {@link #resultsCombiner} on the results of applying
     * method on each of {@link #combinedInstances}
     * @throws Throwable if something is thrown from within one of {@link #combinedInstances}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //apply method on each of combinedInstances
        for (T instance : combinedInstances) {

            final Object result = method.invoke(instance, args);
            this.resultsCombiner.instance(result);
        }

        //combine the results using strategy
        return this.resultsCombiner.combine();
    }
}
