package com.hp.commons.core.graph.impl;

import com.hp.commons.core.graph.impl.combiner.NullTraversalResultsCombiner;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/28/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class NullTraversalResultsCombinerTest {

    /**
     * sanity test
     *
     */
    @Test(timeout = 5000)
    public void testCombine() throws Exception {

        Assert.assertNull(
                "null combiner did not return null",
                NullTraversalResultsCombiner.getInstance().combine(null, null, null));
    }
}
