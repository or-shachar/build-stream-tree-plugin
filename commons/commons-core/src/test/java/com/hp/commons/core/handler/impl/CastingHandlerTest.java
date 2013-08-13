package com.hp.commons.core.handler.impl;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CastingHandlerTest {

    @Test
    public void testApply() throws Exception {

        final CastingHandler<Object, String> caster =
                new CastingHandler<Object, String>(String.class);

        final String string = caster.apply((Object) "string");
        Assert.assertEquals("casting String object to String failed.", String.class, string.getClass());
    }
}
