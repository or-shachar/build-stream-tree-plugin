package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 *
 * the exception to throw when trying to move an immovable node.
 *
 */
public class NotMovable extends TreeException {

    /**
     *
     * @param reason the reason why the move failed or is forbidden
     * @param node to move
     * @param newParent node underneath which the element in "node" param should be placed.
     */
    public NotMovable(String reason, Object node, Object newParent) {
        super("Move of '" + node.getClass().getSimpleName() +
                "' instance '" + node + "' under '" + newParent.getClass().getSimpleName() + "' instance '" +
                newParent + "'failed: " + reason);
    }

    /**
     *
     * @param node to move
     * @param newParent node underneath which the element in "node" param should be placed.
     */
    public NotMovable(Object node, Object newParent) {

        this( node.getClass().getSimpleName() + " type nodes are not movable.",node, newParent);
    }
}
