package com.hp.commons.core.combiner.impl;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class NullCombinationBuilderTest {

    /**
     * sanity make sure it doesn't throw an exception or something
     */
    @Test(timeout = 5000)
    public void test() throws Exception {

        final NullCombinationBuilder<Object> instance =
                NullCombinationBuilder.getInstance();

        instance.instance("whatever");
        instance.instance(null);

        Assert.assertNull("null combiner returned not null value", instance.combine());
    }
}
