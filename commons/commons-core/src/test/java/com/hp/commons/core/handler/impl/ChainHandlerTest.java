package com.hp.commons.core.handler.impl;

import com.hp.commons.core.handler.Handler;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChainHandlerTest {
    @Test
    public void testApply() throws Exception {

        final ChainHandler<Integer, Integer, Integer, String> castAndDouble = new ChainHandler<Integer, Integer, Integer, String>(
            new Handler<Integer, Integer>() {
                @Override
                public Integer apply(Integer node) {
                    return node * 2;
                }
            },
            new Handler<Integer, String>() {
                @Override
                public Integer apply(String node) {
                    return Integer.parseInt(node);
                }
            }
        );

        Assert.assertEquals("casting 2 to int, and doubling it not equal to 4",
                (Object)4,
                castAndDouble.apply("2"));
    }
}
