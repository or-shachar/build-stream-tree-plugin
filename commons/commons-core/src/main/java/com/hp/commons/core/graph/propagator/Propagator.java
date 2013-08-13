package com.hp.commons.core.graph.propagator;

import java.util.Collection;
import com.hp.commons.core.graph.traverser.Traversal;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/24/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 *
 * determines the next nodes to traverse during a graph {@link Traversal}.
 * this effectively determines the shape of the graph by dynamically calculating its edges according
 * to the underlining data structure.
 *
 */
public interface Propagator<T> {

    /**
     *
     * @param source the node whose neighbors we wish to determined
     * @return the neighbors of "source" node, according to concrete implementation.
     */
    public Collection<T> propagate(T source);

}
