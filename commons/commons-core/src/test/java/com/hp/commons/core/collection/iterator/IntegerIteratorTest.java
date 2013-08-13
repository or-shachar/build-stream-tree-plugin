package com.hp.commons.core.collection.iterator;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerIteratorTest {

    @Test(timeout = 5000)
    public void testSanity() throws Exception {

        final IntegerIterator integerIterator = new IntegerIterator();

        Assert.assertTrue("out of integers?", integerIterator.hasNext());
        final Integer first = integerIterator.next();

        Assert.assertTrue("out of integers?", integerIterator.hasNext());
        final Integer second = integerIterator.next();

        Assert.assertTrue("out of integers?", integerIterator.hasNext());
        final Integer third = integerIterator.next();

        //cast to solve ambiguity
        Assert.assertEquals("first and second iterations not consecutive", (Object) second, first + 1);
        Assert.assertEquals("third and second iterations not consecutive", (Object) third, second + 1);

        integerIterator.reset(Integer.MAX_VALUE);

        Assert.assertTrue("should have had one more integer...", integerIterator.hasNext());
        Assert.assertEquals("failed to get max integer",(Object)Integer.MAX_VALUE, integerIterator.next());
        Assert.assertFalse("should have run out of integers...",integerIterator.hasNext());

        integerIterator.reset(0);
        Assert.assertTrue("reset failed to give new elements", integerIterator.hasNext());
        Assert.assertEquals("reset to 0 failed",(Object)0, integerIterator.next());

    }
}
