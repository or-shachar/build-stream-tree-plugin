package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 *
 * when a branch or node in a tree cannot be cloned, but an attempt was made - this is the exception
 * to throw.
 *
 */
public class NotCloneable extends TreeException {

    /**
     *
     * @param node to clone
     * @param newParent the node underneath which to place the new cloned node.
     */
    public NotCloneable(Object node, Object newParent) {
        super("Attempted to clone '" + node.getClass().getSimpleName() +
                "' instance '" + node + "' to parent '" + newParent.getClass().getSimpleName() + "' instance '" +
                newParent + "', but it is not cloneable.");
    }
}
