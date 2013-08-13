package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 *
 * the exception to throw when trying to delete something in the tree that may not be deleted.
 * for example, if the tree is read-only, or if the root or certain types of nodes are protected from
 * deletion.
 *
 */
public class NotDeletable extends TreeException {

    public NotDeletable(String reason, Object vg) {
        super("Deletion of '" + vg.getClass().getSimpleName() + "' instance '" + vg +
                "' failed: " + reason);
    }

    public NotDeletable(Object wrapped) {
        this("Nodes of type '" + wrapped.getClass().getSimpleName() + "' cannot be deleted.", wrapped);
    }
}
