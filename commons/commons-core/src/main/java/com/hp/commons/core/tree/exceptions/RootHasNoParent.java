package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 *
 * exception to throw on attempt to access the parent of root.
 *
 */
public class RootHasNoParent extends TreeException {

    /**
     *
     * @param root the root node whose non-existing parent was accessed
     */
    public RootHasNoParent(Object root) {
        super("node '" + root + "' of type '" + root.getClass().getSimpleName() + "' is root and has no parent.");
    }
}
