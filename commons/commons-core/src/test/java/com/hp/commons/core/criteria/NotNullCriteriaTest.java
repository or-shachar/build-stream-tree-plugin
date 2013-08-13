package com.hp.commons.core.criteria;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotNullCriteriaTest {

    @Test(timeout = 5000)
    public void testIsSuccessful() throws Exception {

        final NotNullCriteria criteria = NotNullCriteria.getInstance();

        Assert.assertFalse("null passed not null criteria", criteria.isSuccessful(null));

        Assert.assertTrue("1 failed not null criteria", criteria.isSuccessful(1));
        Assert.assertTrue("string failed not null criteria", criteria.isSuccessful("bla"));

    }
}
