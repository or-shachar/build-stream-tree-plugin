package com.hp.commons.core.criteria;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class IsTrueCriteriaTest {

    @Test(timeout = 5000)
    public void testIsSuccessful() throws Exception {
        Assert.assertFalse("null is not true", IsTrueCriteria.getInstance().isSuccessful(null));
        Assert.assertFalse("false is not true", IsTrueCriteria.getInstance().isSuccessful(Boolean.FALSE));
        Assert.assertTrue("false is not true", IsTrueCriteria.getInstance().isSuccessful(Boolean.TRUE));
    }
}
