/**
 * this package contains a function abstraction called {@link Handler}, and a few implementations.
 * all other commons-modules that accept a function in general, accept a {@link Handler}.
 * common {@link Handler}s collect elements, and a few implementation of this can be found in the
 * {@link collector} subpackage.
 * this package contains other common {@link Handler}s, such as {@link NoOperationHandler} and other
 * useful common handlers.
 */
package com.hp.commons.core.handler;

import com.hp.commons.core.handler.impl.NoOperationHandler;