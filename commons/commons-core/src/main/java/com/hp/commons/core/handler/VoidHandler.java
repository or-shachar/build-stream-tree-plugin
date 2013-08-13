package com.hp.commons.core.handler;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/25/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 *
 * comfort interface, avoiding declaring Void as a return type for a Handler.
 * if your Handler returns nothing, extend this instead of Handler
 *
 */
public interface VoidHandler<T> extends Handler<Void, T> {}
