package com.hp.commons.core.combiner;

import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/27/12
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO the combine function should be moved to ProxyUtils when we finally create it, and this class removed.
 *
 */
public class CombinerFactory {

    //TODO fix type safety issue CombinationBuilder **<T>**

    /**
     *
     * this function wraps a group of instances all implementing a certain interface with a single interface
     * implementing instance.
     *
     * using this class can save on writing logic that can accept both an instance or a collection of instances,
     * making code more readable and concise.
     *
     * an example: you have a collection of Handlers that you wish to apply, and an API that accepts a single
     * Handler. you could create a class of Handler that accepts a collection and delegates to all of them...
     * or use this proxy. why maintain/document/test a new Handler class?
     *
     * in case it is not obvious, this function uses reflection and proxy, so the returned object is slower
     * than the originals. not suitable for realtime critical systems.
     *
     * @param c class of the interface that each of impls implements and the returned value of this function
     * @param impls numerous implementations of interface c
     * @param resultsCombiner strategy for combining the resulting
     * @param <T> the type of the interface instances being combined, and therefore returned
     * @return an instance of c that delegates logic to all impls
     *
     */
    public static <T> T combine(Class<T> c, Collection<? extends T> impls, CombinationBuilder resultsCombiner) {
        return (T)Proxy.newProxyInstance(
                c.getClassLoader(),
                new Class[] { c },
                new CombiningInvocationHandler(impls, resultsCombiner));
    }
}
