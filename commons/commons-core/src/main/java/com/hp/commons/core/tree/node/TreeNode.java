package com.hp.commons.core.tree.node;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * TODO: should support dynamic operations on the tree
 *
 * <br/>represents tree elements
 *
 * @param <T> type of the TreeNode: this interesting use of recursive definition means that when we create an implementation,
 * we can guarantee the type returned by the "getChildren" method for example
 */
public interface TreeNode<T extends TreeNode<T>> {

    public T getParent();
    public <E extends T> List<E> getChildren();
    public void move(T newParent);
    public void clone(T newParent);

    public void delete();
}
