package com.hp.commons.core.tree.exceptions;


/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 *
 * tree is a container, and when working on a node that is not contained - this
 * is the exception to throw.
 *
 */
public class ElementNotInTreeException extends TreeException {

    /**
     *
     * @param o element that was expected to be in tree but was not.
     */
    public ElementNotInTreeException(Object o) {
        super("element '" + o + "' is not contained in the tree.");
    }
}
