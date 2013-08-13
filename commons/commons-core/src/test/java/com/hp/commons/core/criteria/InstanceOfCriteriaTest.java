package com.hp.commons.core.criteria;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceOfCriteriaTest {

    @Test(timeout = 5000)
    @SuppressWarnings("unchecked")
    public void testIsSuccessful() {

        final InstanceOfCriteria criteria = new InstanceOfCriteria<String>(String.class);

        Assert.assertTrue("string not of type string?", criteria.isSuccessful("string"));
        Assert.assertTrue("null not of type string?", criteria.isSuccessful(null));
        Assert.assertFalse("3 is of type string?", criteria.isSuccessful(3));
    }
}
