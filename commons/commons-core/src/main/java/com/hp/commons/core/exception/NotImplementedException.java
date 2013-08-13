package com.hp.commons.core.exception;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/9/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 *
 * exception that should be thrown when a certain interface method is left unimplemented.
 * for example {@link java.util.Iterator#remove()} which is commonly unimplemented.
 *
 * note that this is very DANGEROUS! you will be lying concerning the interface contract.
 * this is a runtime exception, so there is no static compile time validation.
 *
 * USE AT YOUR OWN RISK.
 *
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }
}
