package com.hp.commons.core.collection;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapEntryImplTest {

    @Test(timeout = 5000)
    public void testGettersSetters() {

        final String s = "bla";
        final Integer i = 39;

        final MapEntryImpl<String, Integer> e = new MapEntryImpl<String, Integer>(s, i);

        Assert.assertEquals(s, e.getKey());
        Assert.assertEquals(i, e.getValue());

        final Integer newValue = 40;

        e.setValue(newValue);

        Assert.assertEquals(newValue, e.getValue());
    }

    @Test(timeout = 5000)
    public void testEquals() {

        Assert.assertEquals(
                new MapEntryImpl<String, Integer>("bla", 1),
                new MapEntryImpl<String, Integer>("bla", 1));

        Assert.assertFalse(
                "map entries with different values determined equal",
                new MapEntryImpl<String, Integer>("bla", 1).equals(
                        new MapEntryImpl<String, Integer>("bla", 2)));

        Assert.assertFalse(
                "map entries with different keys determined equal",
                new MapEntryImpl<String, Integer>("bla", 1).equals(
                    new MapEntryImpl<String, Integer>("bla1", 1)));

        Assert.assertFalse(
                "map entries with different keys determined equal",
                new MapEntryImpl<String, Integer>("bla", 1).equals(
                    new MapEntryImpl<String, Integer>("bla1", 1)));

        Assert.assertFalse(
                "completely different entries determined to be equal",
                new MapEntryImpl<String, Integer>("bla", 1).equals(
                    new MapEntryImpl<String, Integer>("bla1", 2)));
    }

    @Test(timeout = 5000)
    public void testHashCode() {

        Assert.assertEquals(
                new MapEntryImpl<String, Integer>("bla", 1).hashCode(),
                new MapEntryImpl<String, Integer>("bla", 1).hashCode());

        Assert.assertFalse(
                "map entries with different values determined hashcode equal",
                new MapEntryImpl<String, Integer>("bla", 1).hashCode() ==
                        new MapEntryImpl<String, Integer>("bla", 2).hashCode());

        Assert.assertFalse(
                "map entries with different keys determined hashcode equal",
                new MapEntryImpl<String, Integer>("bla", 1).hashCode() ==
                        new MapEntryImpl<String, Integer>("bla1", 1).hashCode());

        Assert.assertFalse(
                "map entries with different keys determined hashcode equal",
                new MapEntryImpl<String, Integer>("bla", 1).hashCode() ==
                    new MapEntryImpl<String, Integer>("bla1", 1).hashCode());

        Assert.assertFalse(
                "completely different entries determined to be hashcode equal",
                new MapEntryImpl<String, Integer>("bla", 1).hashCode() ==
                    new MapEntryImpl<String, Integer>("bla1", 2).hashCode());
    }
}
