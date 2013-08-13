package com.hp.commons.core.unique;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SequentialIntegerKeysFactoryTest {

    /**
     * sanity tests, just to make sure doesn't throw nulls and stuff...
     */
    @Test(timeout = 5000)
    public void testGetKey() throws Exception {

        final KeyFactory<String, Integer> stringSequentialIntegerKeysFactory =
                new SequentialIntegerKeysFactory<String>();

        int i1 = stringSequentialIntegerKeysFactory.getKey(null, null);
        int i2 = stringSequentialIntegerKeysFactory.getKey("some value");
        int i3 = stringSequentialIntegerKeysFactory.getKey(Arrays.asList("some value"));

        Assert.assertEquals(i2, i1 + 1);
        Assert.assertEquals(i3, i2 + 1);
    }

}
