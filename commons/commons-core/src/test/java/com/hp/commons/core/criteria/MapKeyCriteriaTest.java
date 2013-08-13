package com.hp.commons.core.criteria;

import com.hp.commons.core.collection.MapEntryImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapKeyCriteriaTest {
    @Test(timeout = 5000)
    public void testIsSuccessful() throws Exception {

        final MapKeyCriteria<Boolean, String> criteria =
                new MapKeyCriteria<Boolean, String>(IsTrueCriteria.getInstance());

        Assert.assertFalse("key criteria with null entry returned true",criteria.isSuccessful(null));

        Map.Entry<Boolean, String> nullEntry = new MapEntryImpl<Boolean, String>(null, "null");
        Assert.assertFalse("key criteria with null key returned true",criteria.isSuccessful(nullEntry));

        Map.Entry<Boolean, String> trueEntry = new MapEntryImpl<Boolean,String>(true,"true");
        Assert.assertTrue("key criteria with true key returned false",criteria.isSuccessful(trueEntry));

        Map.Entry<Boolean, String> falseEntry = new MapEntryImpl<Boolean,String>(false,"false");
        Assert.assertFalse("key criteria with false key returned true",criteria.isSuccessful(falseEntry));

    }
}
