package com.hp.commons.core.combiner.impl;

import com.hp.commons.core.collection.factory.HashSetFactory;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionCombinationBuilderTest {

    @Test(timeout = 5000)
    public void test() throws Exception {

        final CollectionCombinationBuilder<String> combiner =
                new CollectionCombinationBuilder<String>(new HashSetFactory());

        combiner.instance(Arrays.asList("1", "2", "3"));
        combiner.instance(Arrays.asList("1", "2", "3", "4"));
        combiner.instance(Arrays.asList("5"));

        final Collection<String> combined = combiner.combine();

        Assert.assertEquals("return collection type didn't match collection factory type",
                HashSet.class, combined.getClass());

        Assert.assertEquals("combination of lists was incorrect",
                new HashSet<String>(Arrays.asList("1","2","3","4","5")),
                combined);
    }
}
