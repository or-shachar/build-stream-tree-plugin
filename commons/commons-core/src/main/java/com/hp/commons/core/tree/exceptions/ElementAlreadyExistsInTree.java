package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 *
 * tree is a container of some sort, and a strict implementation might want to decline
 * adding a previously added element.
 *
 */
public class ElementAlreadyExistsInTree extends TreeException {

    public ElementAlreadyExistsInTree(Object dataToAdd) {
        super("element '" + dataToAdd + "' already exists in tree.");
    }
}
