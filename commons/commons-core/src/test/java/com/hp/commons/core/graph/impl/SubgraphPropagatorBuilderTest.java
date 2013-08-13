package com.hp.commons.core.graph.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.graph.propagator.MapBasedPropagator;
import com.hp.commons.core.handler.Handler;
import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/28/12
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubgraphPropagatorBuilderTest {
    @Test
    public void testBuild() throws Exception {

        Map<Integer, Collection<Integer>> map =
                new HashMap<Integer, Collection<Integer>>();

        //should not be picked up
        map.put(1, Arrays.asList(3));
        map.put(3, Arrays.asList(5));
        map.put(5, Arrays.asList(7));

        //should be picked up entirely
        map.put(11, Arrays.asList(13));
        map.put(13, Arrays.asList(15));
        map.put(15, Arrays.asList(18));

        //should be picked up entirely
        map.put(21, Arrays.asList(22));
        map.put(22, Arrays.asList(23));
        map.put(23, Arrays.asList(25));
        map.put(25, Arrays.asList(28));

        //should be picked up partially
        map.put(31, Arrays.asList(32));
        map.put(32, Arrays.asList(33));
        map.put(33, Arrays.asList(35));
        map.put(35, Arrays.asList(37));

        final MapBasedPropagator<Integer> build = new SubgraphPropagatorBuilder<Integer>(
                new MapBasedPropagator<Integer>(map),
                new Handler<Boolean, Integer>() {
                    @Override
                    public Boolean apply(Integer node) {
                        return node % 2 == 0;
                    }
                },
                Arrays.asList(1, 11, 21, 31)
        ).build();

        Assert.assertEquals("builder did not collect chains appropriately.",
                new HashSet<Integer>(
                        Arrays.asList(
                                11,13,15,18,
                                21,22,23,25,28,
                                31,32
                        )
                ),
                build.getMap().keySet()
        );
    }
}
