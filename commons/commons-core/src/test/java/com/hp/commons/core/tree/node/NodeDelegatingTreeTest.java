package com.hp.commons.core.tree.node;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeDelegatingTreeTest {

    @Test
    public void testDelegation() {

        final TestNode tester = new TestNode();

        final NodeDelegatingTree<TestNode> tree =
                new NodeDelegatingTree<TestNode>(tester);

        Assert.assertSame("root did not result in constructor arg", tree.getRoot(), tester);

        Assert.assertSame("getParent did not delegate", tree.getParent(tester), tester);

        Assert.assertEquals("getChildren did not delegate",
                tester,
                tree.getChildren(tester).iterator().next());

        Assert.assertFalse("test initialization failure, " +
                "isClone should be false before clone()", tester.isClone());
        tree.clone(tester, tester);
        Assert.assertTrue("clone method did not delegate", tester.isClone());

        Assert.assertFalse("test initialization failure, " +
                "isDelete should be false before delete()", tester.isDelete());
        tree.delete(tester);
        Assert.assertTrue("delete method did not delegate", tester.isDelete());

        Assert.assertFalse("test initialization failure, " +
                "isMove should be false before move()", tester.isMove());
        tree.move(tester, tester);
        Assert.assertTrue("move method did not delegate", tester.isMove());
    }

    private static class TestNode implements TreeNode<TestNode> {

        private boolean move = false;
        private boolean clone = false;
        private boolean delete = false;

        public TestNode() {

        }

        @Override
        public TestNode getParent() {
            return this;
        }

        @Override
        public <E extends TestNode> List<E> getChildren() {
            return Arrays.asList((E)this);
        }

        @Override
        public void move(TestNode newParent) {
            this.move = true;
        }

        @Override
        public void clone(TestNode newParent) {
            this.clone = true;
        }

        @Override
        public void delete() {
            this.delete = true;
        }

        public boolean isMove() {
            return move;
        }

        public boolean isClone() {
            return clone;
        }

        public boolean isDelete() {
            return delete;
        }
    }
}
