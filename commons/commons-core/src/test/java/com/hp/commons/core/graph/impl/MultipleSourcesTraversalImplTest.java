package com.hp.commons.core.graph.impl;

import com.hp.commons.core.graph.impl.traversal.MultipleSourcesTraversalImpl;
import com.hp.commons.core.graph.propagator.MapBasedPropagator;
import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/28/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class MultipleSourcesTraversalImplTest {

    public static final String UNVISITED_ERROR = "node that should have been visited, wasn't";

    @Test
    public void testTraverse() throws Exception {

        Map<com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable, Collection<com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable>> map =
            new HashMap<com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable, Collection<com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable>>();

        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain1link1 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("11");
        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain1link2 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("12");
        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain1link3 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("13");
        map.put(chain1link1, Arrays.asList(chain1link2));
        map.put(chain1link2, Arrays.asList(chain1link3));

        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain2link1 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("21");
        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain2link2 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("22");
        final com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable chain2link3 = new com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable("23");
        map.put(chain2link1, Arrays.asList(chain2link2));
        map.put(chain2link2, Arrays.asList(chain2link3));

        final MultipleSourcesTraversalImpl<Void, GraphNodeTestVisitor.TestVisitable> traversal =
            new MultipleSourcesTraversalImpl<Void, com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable>(
            TraversalFactory.getTraversal(
                    new GraphNodeTestVisitor(),
                    new MapBasedPropagator<com.hp.commons.core.graph.impl.GraphNodeTestVisitor.TestVisitable>(map)
            )
        );

        traversal.traverse(Arrays.asList(chain1link1, chain2link2));

        Assert.assertFalse("chain2link1 node that should not have been visited, was",
                chain2link1.visited());
        Assert.assertTrue("chain2link2 " + UNVISITED_ERROR, chain2link2.visited());
        Assert.assertTrue("chain2link3 " + UNVISITED_ERROR, chain2link3.visited());

        Assert.assertTrue("chain1link1 " + UNVISITED_ERROR, chain1link1.visited());
        Assert.assertTrue("chain1link2 " + UNVISITED_ERROR, chain1link2.visited());
        Assert.assertTrue("chain1link3 " + UNVISITED_ERROR, chain1link3.visited());
    }

}
