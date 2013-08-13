package com.hp.commons.core.graph.impl;

import com.hp.commons.core.graph.impl.traversal.NonRepeatingTraversal;
import com.hp.commons.core.graph.propagator.MapBasedPropagator;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/28/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class NonRepeatingTraversalTest {

    public static final String VISITATION_ERROR = "node should be visited exactly once in non-repeating traversal";

    @Test(timeout = 5000)
    public void testApply() throws Exception {

        Map<GraphNodeTestVisitor.TestVisitable, Collection<GraphNodeTestVisitor.TestVisitable>> map =
                new HashMap<GraphNodeTestVisitor.TestVisitable, Collection<GraphNodeTestVisitor.TestVisitable>>();

        final GraphNodeTestVisitor.TestVisitable a = new GraphNodeTestVisitor.TestVisitable("a");
        final GraphNodeTestVisitor.TestVisitable b = new GraphNodeTestVisitor.TestVisitable("b");
        final GraphNodeTestVisitor.TestVisitable c = new GraphNodeTestVisitor.TestVisitable("c");

        map.put(a, Arrays.asList(b));
        map.put(b, Arrays.asList(c));
        map.put(c, Arrays.asList(a));

        //this test has the potential to infinitely loop, but it's ok because of timeout
        new NonRepeatingTraversal(
            TraversalFactory.getTraversal(
                    new GraphNodeTestVisitor(),
                    new MapBasedPropagator<GraphNodeTestVisitor.TestVisitable>(map)
            )
        ).apply(a);

        Assert.assertEquals(VISITATION_ERROR, 1, a.visitationsCount());
        Assert.assertEquals(VISITATION_ERROR, 1, b.visitationsCount());
        Assert.assertEquals(VISITATION_ERROR, 1, c.visitationsCount());
    }
}
