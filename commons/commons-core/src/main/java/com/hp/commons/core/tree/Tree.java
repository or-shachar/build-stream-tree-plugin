package com.hp.commons.core.tree;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 *
 * abstraction that allows to store data in a tree structure
 *
 * @param <D> the type of data stored in tree structure
 */
public interface Tree<D> {

    /**
     *
     * @return root of the tree.
     */
    public D getRoot();

    /**
     *
     * @param data whose tree-children we'd like to retrieve
     * @return the children of "data" in the tree
     */
    public List<D> getChildren(D data);

    /**
     *
     * @param data whose tree-parent we'd like to retrieve
     * @return the parent of "data" in the tree
     */
    public D getParent(D data);

    /**
     *
     * @param parentNode node to which we should add "dataToAdd" as a child
     * @param dataToAdd node which we should place under "parentNode" in the tree
     */
    public void add(D parentNode, D dataToAdd);

    /**
     *
     * @param dataSubtreeToMove node already stored in the tree, whose subtree should be moved
     * @param newParent node under which the subtree rooted at "dataSubtreeToMove" should be moved.
     */
    public void move(D dataSubtreeToMove, D newParent);

    /**
     *
     * @param dataSubtreeToClone node already stored in the tree, that should be cloned
     * @param newParent node under which the clone of "dataSubtreeToClone" should be placed.
     */
    public void clone(D dataSubtreeToClone, D newParent);

    /**
     *
     * @param subtreeToDelete the node whose rooted subtree should be deleted from the tree.
     */
    public void delete(D subtreeToDelete);
}
