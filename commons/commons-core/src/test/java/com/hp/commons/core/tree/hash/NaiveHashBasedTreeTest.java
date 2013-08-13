package com.hp.commons.core.tree.hash;

import com.hp.commons.core.exception.NotImplementedException;
import com.hp.commons.core.tree.Tree;
import com.hp.commons.core.tree.exceptions.ElementAlreadyExistsInTree;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class NaiveHashBasedTreeTest {

    private NaiveHashBasedTree<Integer> tree;

    @Before
    public void initializeTest() {

        tree = new NaiveHashBasedTree<Integer>(1);

        tree.add(1, 10);
        tree.add(1, 11);

        tree.add(10, 101);
        tree.add(10, 102);
        tree.add(10, 103);
    }

    @Test(timeout = 5000, expected = ElementAlreadyExistsInTree.class)
    public void testCircle() throws Exception {

        tree.add(10, 11);
    }

    @Test(timeout = 5000)
    public void testAdd() throws Exception {

        Assert.assertEquals(tree.getChildren(1), Arrays.asList(10,11));
        Assert.assertEquals(tree.getChildren(10), Arrays.asList(101, 102, 103));
    }

    @Test(timeout = 5000)
    public void testDelete() throws Exception {

        tree.delete(10);

        Assert.assertEquals(tree.getChildren(1), Arrays.asList(11));
    }

    @Test(timeout = 5000)
    public void testMove() throws Exception {

        tree.move(11, 10);

        Assert.assertEquals(tree.getChildren(1), Arrays.asList(10));
        Assert.assertEquals(tree.getChildren(10), Arrays.asList(101, 102, 103, 11));
    }

    @Test(timeout = 5000, expected = NotImplementedException.class)
    public void testCopy() throws Exception {

        tree.clone(11, 11);
    }



}
