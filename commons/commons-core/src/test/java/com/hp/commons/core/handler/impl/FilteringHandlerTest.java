package com.hp.commons.core.handler.impl;

import com.hp.commons.core.criteria.InstanceOfCriteria;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilteringHandlerTest {

    @Test(timeout = 5000)
    public void testApply() throws Exception {

        final FilteringHandler filter =
                new FilteringHandler<String>(
                        new InstanceOfCriteria<String>(String.class));

        Assert.assertNull("string filter passed integer",filter.apply(1));
        Assert.assertEquals("string filter failed to pass string", "string", filter.apply("string"));
    }
}
