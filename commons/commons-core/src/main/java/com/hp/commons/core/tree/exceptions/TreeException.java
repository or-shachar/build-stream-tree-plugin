package com.hp.commons.core.tree.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 *
 * base class for all tree-related exceptions.
 *
 */
public abstract class TreeException extends RuntimeException {

    /**
     *
     * @see RuntimeException#RuntimeException()
     */
    public TreeException(String message) {
        super(message);
    }

    /**
     *
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public TreeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public TreeException(Throwable cause) {
        super(cause);
    }
}
