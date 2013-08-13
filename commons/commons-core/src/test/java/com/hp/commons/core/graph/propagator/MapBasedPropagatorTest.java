package com.hp.commons.core.graph.propagator;

import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapBasedPropagatorTest {



    @Test//(timeout = 5000)
    public void testPropagate() throws Exception {

        Map<Integer, Collection<Integer>> map =
                new HashMap<Integer, Collection<Integer>>();

        map.put(1, Arrays.asList(10, 11, 12));
        map.put(10, Arrays.asList(11));
        map.put(11, Arrays.asList(13));

        final MapBasedPropagator<Integer> propagator =
                new MapBasedPropagator<Integer>(map);

        Assert.assertEquals(new HashSet(Arrays.asList(10, 11, 12)), propagator.propagate(1));
        Assert.assertEquals(new HashSet(Arrays.asList(11)), propagator.propagate(10));
        Assert.assertEquals(new HashSet(Arrays.asList(13)), propagator.propagate(11));

        Assert.assertTrue("leaf had propagations", propagator.propagate(12).isEmpty());
        Assert.assertTrue("leaf had propagations", propagator.propagate(13).isEmpty());
    }
}
