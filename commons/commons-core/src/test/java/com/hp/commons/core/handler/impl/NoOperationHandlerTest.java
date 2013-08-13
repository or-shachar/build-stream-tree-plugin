package com.hp.commons.core.handler.impl;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoOperationHandlerTest {

    /*
    sanity, make sure doesn't throw any exception or anything
     */
    @Test(timeout = 5000)
    public void testApply() throws Exception {
        NoOperationHandler.getInstance().apply(null);
    }
}
