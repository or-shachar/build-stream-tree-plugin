package com.hp.commons.core.tree.node;

import java.util.List;
import com.hp.commons.core.tree.Tree;
import com.hp.commons.core.exception.NotImplementedException;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 *
 * Tree implementation that delegates operation logic to the nodes.
 *
 * @param <D> the type of the node of the tree
 */
public class NodeDelegatingTree<D extends TreeNode<D>> implements Tree<D> {

    /**
     * root of the tree
     */
    private D root;

    /**
     *
     * @param root of the tree
     */
    public NodeDelegatingTree(D root) {
        this.root = root;
    }

    @Override
    public D getRoot() {
        return this.root;
    }

    @Override
    public List<D> getChildren(D data) {

        final List<D> children = data.getChildren();

        return children;
    }

    @Override
    public D getParent(D data) {
        return data.getParent();
    }

    @Override
    public void add(D parentNode, D dataToAdd) {
        throw new NotImplementedException();
    }

    @Override
    public void move(D dataSubtreeToMove, D newParent) {
        dataSubtreeToMove.move(newParent);
    }

    @Override
    public void clone(D dataSubtreeToClone, D newParent) {
        dataSubtreeToClone.clone(newParent);
    }

    @Override
    public void delete(D subtreeToDelete) {
        subtreeToDelete.delete();
    }
}
