package com.hp.commons.core.handler.collector;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 * CollectingHandler only extends NonTransformingCollector - so no need for separate test
*/
public class CollectingHandlerTest {

    @Test(timeout = 5000)
    public void testApply() throws Exception {

        Collection<String> col = new ArrayList<String>();
        final CollectingHandler<String> handler = new CollectingHandler<String>(col);

        final String collected = "collect me";

        handler.apply(collected);

        Assert.assertTrue("string was not added to collection", col.contains(collected));
        Assert.assertSame("constructor argument and getter are not same instance", col, handler.getCollection());

        final String collectedToOtherCollection = "not contained";

        handler.setCollection(new ArrayList<String>());
        handler.apply(collectedToOtherCollection);

        Assert.assertFalse("string was collected to original collection, " +
                "even though set was used to collect to a different collection",
                col.contains(collectedToOtherCollection));
    }
}
