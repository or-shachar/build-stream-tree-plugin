package com.hp.commons.core.collection;

import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.exception.NotImplementedException;
import com.hp.commons.core.handler.Handler;
import org.junit.Assert;
import org.junit.Test;

import java.net.ServerSocket;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/22/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtilsTest {

    @Test(timeout = 5000)
    public void testIsPrefix() throws Exception {

        final List<String> prefix = Arrays.asList("a", "b", "c");
        final List<String> suffix = Arrays.asList("d", "e", "f");

        Assert.assertFalse(
                prefix + " is not a prefix of " + suffix + ", but was incorrectly determined to be.",
                CollectionUtils.isPrefix(prefix, suffix)
        );

        final ArrayList<String> list = new ArrayList<String>(prefix.size() + suffix.size());
        list.addAll(prefix);
        list.addAll(suffix);

        Assert.assertTrue(
                prefix + " is a prefix of " + list + ", but was incorrectly determined not to be.",
                CollectionUtils.isPrefix(prefix, list)
        );

    }

    @Test(timeout = 5000)
    public void testAreEqual() throws Exception {

        Assert.assertTrue("equal elements determined to be different",
                CollectionUtils.areEqual(new Integer[] {1, 1, 1, 1, 1}));

        Assert.assertFalse("different elements determined to be equal",
                CollectionUtils.areEqual(new Integer[] {1, 1, 2, 1, 1}));

        Assert.assertTrue("all nulls determined to be different",
                CollectionUtils.areEqual(new Integer[] {null, null, null}));

        Assert.assertTrue("empty collection determined to be different",
                CollectionUtils.areEqual(new Integer[] {}));

        Assert.assertTrue("unary collection determined to be different",
                CollectionUtils.areEqual(new Integer[] {1}));
    }

    @Test(timeout = 5000)
    public void testAreDifferent() throws Exception {

        Assert.assertFalse("equal elements determined to be different",
                CollectionUtils.areDifferent(new Integer[]{1, 1, 1, 1, 1}));

        Assert.assertTrue("different elements determined to be equal",
                CollectionUtils.areDifferent(new Integer[]{1, 1, 2, 1, 1}));

        Assert.assertFalse("all nulls determined to be different",
                CollectionUtils.areDifferent(new Integer[]{null, null, null}));

        Assert.assertFalse("empty collection determined to be different",
                CollectionUtils.areDifferent(new Integer[]{}));

        Assert.assertFalse("unary collection determined to be different",
                CollectionUtils.areDifferent(new Integer[]{1}));
    }

    @Test(timeout = 5000)
    public void testNextOrNull() throws Exception {

        Iterator<Integer> iter = Arrays.asList(1).iterator();

        Assert.assertEquals(1, CollectionUtils.nextOrNull(iter));
        Assert.assertEquals(null, CollectionUtils.nextOrNull(iter));
    }

    @Test(timeout = 5000)
    public void testCombineArrays() throws Exception {

        final Integer[] prefix = {1, 2, 3};

        Assert.assertArrayEquals(
                "appending empty array to " +
                        Arrays.toString(prefix) +
                        " resulted in something not " +
                        Arrays.toString(prefix),
                prefix,
                CollectionUtils.combineArrays(prefix));

        final Integer[] suffix = {4, 5, 6};

        final Integer[] expected = {1, 2, 3, 4, 5, 6};

        Assert.assertArrayEquals(
                "combination of " + Arrays.toString(prefix) + " with " +
                        Arrays.toString(suffix) +
                        " failed to result in " +
                        Arrays.toString(expected),
                expected,
                CollectionUtils.combineArrays(
                        prefix,
                        suffix
                )
        );
    }

    @Test(timeout = 5000)
    public void testMap() throws Exception {

        final List<Integer> postTransform = Arrays.asList(-1, -2, -3);
        final List<Integer> preTransform = Arrays.asList(1, 2, 3);
        Assert.assertEquals(
            "handler of *-1 failed to transform " + preTransform + " to " + postTransform,
            postTransform,
            CollectionUtils.map(
                preTransform,
                new Handler<Object, Integer>() {
                        @Override
                        public Object apply(Integer node) {
                            return node * -1;
                        }
                    }));
    }

    @Test(timeout = 5000)
    public void testMapToMap() throws Exception {

        final Map<Integer, Integer> map = CollectionUtils.mapToMap(
                Arrays.asList(1, 2, 3),
                new Handler<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer node) {
                        return node * -1;
                    }
                });

        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            Assert.assertEquals(
                    "value != key*-1 but *-1 handler used on key...",
                    //cast to solve multiple method match ambiguity
                    (Object)entry.getKey(),
                    entry.getValue() * -1);
        }
    }

    @Test(timeout = 5000)
    public void testMax() throws Exception {

        final Handler<Comparable, String> stringLengthMeasuringHandler =
            new Handler<Comparable, String>() {
                @Override
                public Comparable apply(String node) {
                    return node.length();
                }
            }
        ;

        Assert.assertEquals(
            new MapEntryImpl("max",3),
            CollectionUtils.max(
                Arrays.asList("a","max", "aa", "aa", "a"),
                stringLengthMeasuringHandler
            )
        );

        Assert.assertNull(
            CollectionUtils.max(
                Collections.<String>emptyList(),
                stringLengthMeasuringHandler
            )
        );

    }

    @Test(timeout = 5000)
    public void testContainsAny() throws Exception {

        Assert.assertTrue("1,2,3 doesn't contain 1",
                CollectionUtils.containsAny(
                        Arrays.asList(1, 2, 3),
                        Arrays.asList(1)));

        Assert.assertTrue("1,2,3 doesn't contain 2",
                CollectionUtils.containsAny(
                    Arrays.asList(1,2,3),
                    Arrays.asList(2)));

        Assert.assertTrue("1,2,3 doesn't contain 3",
                CollectionUtils.containsAny(
                    Arrays.asList(1,2,3),
                    Arrays.asList(3)));

        Assert.assertTrue("1,2,3 doesn't contain 1,3",
                CollectionUtils.containsAny(
                    Arrays.asList(1,2,3),
                    Arrays.asList(1,3)));

        Assert.assertTrue("1,2,3 doesn't contain 1,2",
                CollectionUtils.containsAny(
                    Arrays.asList(1,2,3),
                    Arrays.asList(1,2)));

        Assert.assertTrue("1,2,3 doesn't contain 2,3",
                CollectionUtils.containsAny(
                    Arrays.asList(1,2,3),
                    Arrays.asList(2,3)));

        Assert.assertFalse("1,2,3 contains 4",
                CollectionUtils.containsAny(
                        Arrays.asList(1, 2, 3),
                        Arrays.asList(4)));

    }

    @Test(timeout = 5000)
    public void testIntersect() throws Exception {

        final List<Integer> c1 = Arrays.asList(1, 2, 3, 4);
        final List<Integer> c2 = Arrays.asList(2, 4, 6, 8);
        final List<Integer> intersection = Arrays.asList(2, 4);
        Assert.assertEquals(
            "intersection of " + c1 + " with " + c2 + " differs from " + intersection,
            intersection,
            CollectionUtils.intersect(
                c1,
                c2
            )
        );

    }

    @Test(timeout = 5000)
    public void testFilter() throws Exception {

        //array list impl supports iterator.remove()
        final List<Integer> items = new ArrayList<Integer>(
                Arrays.asList(0, 1, -1, 2, -2, 3, -3, -4, -5, -6));

        CollectionUtils.filter(
            items,
            new Criteria<Integer>() {
                @Override
                public boolean isSuccessful(Integer tested) {
                    return tested > 0;
                }
            }
        );

        for (Integer i : items) {
            Assert.assertTrue(i + " <= 0, after all non-positives have been filtered out.",i > 0);
        }
    }

    @Test(timeout = 5000)
    public void testReverseMap() throws Exception {

        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

        map.put(0, Arrays.asList(3,4,5));
        map.put(1, Arrays.asList(10,11,12));
        map.put(2, Arrays.asList(20,21,22));

        final Map<Integer, HashSet<Integer>> reversed = CollectionUtils.reverseMap(map);
        for (Map.Entry<Integer, HashSet<Integer>> reversedEntry: reversed.entrySet()) {
            Integer num = reversedEntry.getKey();
            for (Integer tensCount : reversedEntry.getValue()) {
                Assert.assertEquals(
                        "reversing mapping of tens digit to number did not result in " +
                        "mapping of number to tens digit, for the number " + num,
                        num / 10,
                        (long)tensCount);
            }
        }
    }

    @Test(timeout = 5000)
    public void testReversePolyndromMap() throws Exception {

        Map<Integer, HashSet<Integer>> map1 = new HashMap<Integer, HashSet<Integer>>();

        map1.put(0, new HashSet(Arrays.asList(1)));
        map1.put(1, new HashSet(Arrays.asList(0)));

        Assert.assertEquals(
                "reversing a 'polyndrom' map resulted in non identical maps.",
                map1,
                CollectionUtils.reverseMap(map1));

    }

}
