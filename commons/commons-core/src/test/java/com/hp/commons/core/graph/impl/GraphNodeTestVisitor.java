package com.hp.commons.core.graph.impl;

import com.hp.commons.core.handler.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/28/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class is used to test graph traversals
 */
public class GraphNodeTestVisitor implements Handler<Void, GraphNodeTestVisitor.TestVisitable> {

    @Override
    public Void apply(TestVisitable node) {

        node.visit();

        return null;
    }

    public static class TestVisitable {

        private int visited = 0;
        private String name;

        public TestVisitable(String s) {
            name = s;
        }

        public void visit() {

            System.out.println(name);
            visited++;
        }

        public boolean visited() {

            return visited > 0;
        }

        public int visitationsCount() {

            return visited;
        }

    }
}
