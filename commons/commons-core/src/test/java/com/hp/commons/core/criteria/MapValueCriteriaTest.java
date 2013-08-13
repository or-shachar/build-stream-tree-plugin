package com.hp.commons.core.criteria;

import com.hp.commons.core.collection.MapEntryImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapValueCriteriaTest {

    @Test(timeout = 5000)
    public void testIsSuccessful() throws Exception {

        final MapValueCriteria<String, Boolean> criteria =
                new MapValueCriteria<String, Boolean>(IsTrueCriteria.getInstance());

        Assert.assertFalse("value criteria with null entry returned true",criteria.isSuccessful(null));

        Map.Entry<String, Boolean> nullEntry = new MapEntryImpl<String, Boolean>("true", null);
        Assert.assertFalse("value criteria with null value returned true",criteria.isSuccessful(nullEntry));

        Map.Entry<String, Boolean> trueEntry = new MapEntryImpl<String, Boolean>("true",true);
        Assert.assertTrue("value criteria with true value returned false",criteria.isSuccessful(trueEntry));

        Map.Entry<String, Boolean> falseEntry = new MapEntryImpl<String, Boolean>("false",false);
        Assert.assertFalse("value criteria with false value returned true", criteria.isSuccessful(falseEntry));

    }
}
