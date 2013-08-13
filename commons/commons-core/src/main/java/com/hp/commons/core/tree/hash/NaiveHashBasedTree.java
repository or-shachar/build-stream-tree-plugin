package com.hp.commons.core.tree.hash;

import com.hp.commons.core.exception.NotImplementedException;
import com.hp.commons.core.tree.Tree;
import com.hp.commons.core.tree.exceptions.ElementAlreadyExistsInTree;
import com.hp.commons.core.tree.exceptions.ElementNotInTreeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 *
 * hash based tree implementation. each element is stored as a key in a map, and the values are its children.
 * efficient in insertion, addition, move, but very bad in removal (each node must be removed).
 *
 */
public class NaiveHashBasedTree<D> implements Tree<D> {

    /**
     * default used for initial size of the children collection for each node.
     * the size of the tree is exponential in the size of this element - so should be small.
     */
    private static final int DEFAULT_WIDTH_CARDINALITY = 5;

    /**
     * root of the tree
     */
    private D root;

    /**
     * map from node->children
     */
    private HashMap<D, List<D>> dataChildMapping;

    /**
     * map from child->parent
     */
    private HashMap<D, D> dataParentMapping;

    /**
     * initial size of child container per element
     */
    private int cardinality;

    /**
     *
     * @param root of the tree
     */
    public NaiveHashBasedTree(D root) {
        this(root, DEFAULT_WIDTH_CARDINALITY);
    }

    /**
     *
     * @param root of the tree
     * @param cardinality initial size of child container per element
     */
    public NaiveHashBasedTree(D root, int cardinality) {

        init(root, cardinality);
    }

    //TODO move into constructor, no reason for this to be an external function
    private void init(D root, int cardinality) {

        this.root = root;
        this.cardinality = cardinality;

        this.dataChildMapping = new HashMap<D, List<D>>();
        dataChildMapping.put(this.root, new ArrayList<D>(this.cardinality));

        this.dataParentMapping = new HashMap<D, D>();
        dataParentMapping.put(this.root, null);
    }

    @Override
    public D getRoot() {
        return this.root;
    }

    /**
     *
     * @param data whose tree-children we'd like to retrieve
     * @return an unmodifiable set of "data"'s children.
     */
    @Override
    public List<D> getChildren(D data) {
        //_getChildren is used internally by the class, because the getter returns an unmodifiable set.
        return Collections.unmodifiableList(_getChildren(data));
    }

    @Override
    public D getParent(D data) {
        return this.dataParentMapping.get(data);
    }

    /**
     *
     * @param parentNode node to which we should add "dataToAdd" as a child
     * @param dataToAdd node which we should place under "parentNode" in the tree
     *
     * @throws ElementAlreadyExistsInTree
     * @throws ElementNotInTreeException
     */
    @Override
    public void add(D parentNode, D dataToAdd) {

        //verify that dataToAdd isn't already in tree, potentially creating a circle
        if (getParent(dataToAdd) != null || _getChildren(dataToAdd) != null) {
            throw new ElementAlreadyExistsInTree(dataToAdd);
        }

        //verify that parentNode is in the tree (any node in tree has children container even if empty)
        final List<D> children = _getChildren(parentNode);

        if (children == null) {
            throw new ElementNotInTreeException(parentNode);
        }

        children.add(dataToAdd);
        this.dataParentMapping.put(dataToAdd, parentNode);

        //maintain contract: any element in the tree already has a container for its kids allocated
        this.dataChildMapping.put(dataToAdd, new ArrayList<D>(cardinality));
    }

    @Override
    public void move(D dataSubtreeToMove, D newParent) {

        //remove from current parent children
        _getChildren(getParent(dataSubtreeToMove)).remove(dataSubtreeToMove);

        //add to children of new parent
        _getChildren(newParent).add(dataSubtreeToMove);

        //set new parent as parent
        this.dataParentMapping.put(dataSubtreeToMove, newParent);
    }

    /**
     *
     * this method is not implemented
     *
     * @param dataSubtreeToClone node already stored in the tree, that should be cloned
     * @param newParent node under which the clone of "dataSubtreeToClone" should be placed.
     *
     * @throws NotImplementedException
     */
    @Override
    public void clone(D dataSubtreeToClone, D newParent) {
        //TODO: not implemented yet
        throw new NotImplementedException();
    }

    @Override
    public void delete(D subtreeToDelete) {

        //recursively delete all children - expensive operation: can't just delete the parent
        //because the "deleted" subtree will still exist in the maps.
        final List<D> children = _getChildren(subtreeToDelete);
        while (!children.isEmpty()) {

            //the list keeps getting smaller and smaller,
            // the recursive call of the child changes its relative parent's children list
            // (which absolutely is this children list)
            delete(children.get(0));
        }

        //delete the actual node itself
        _getChildren(getParent(subtreeToDelete)).remove(subtreeToDelete);
        this.dataParentMapping.remove(subtreeToDelete);
        this.dataChildMapping.remove(subtreeToDelete);
    }

    /**
     *
     * @param data whose children should be retrieved
     * @return children of "data"
     */
    private List<D> _getChildren(D data) {
        return this.dataChildMapping.get(data);
    }
}
