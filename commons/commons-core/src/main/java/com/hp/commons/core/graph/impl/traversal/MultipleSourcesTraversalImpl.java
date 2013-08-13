package com.hp.commons.core.graph.impl.traversal;

import com.hp.commons.core.graph.traverser.MultipleSourcesTraversal;
import com.hp.commons.core.graph.traverser.Traversal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/26/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 *
 * naive simple implementation of multiple sources traversal: just activates a pre-defined (during initialization)
 * traversal on each of the specified sources.
 *
 * this class is usually combined with NonRepeatingTraversal
 */
public class MultipleSourcesTraversalImpl<R, T> implements MultipleSourcesTraversal<R, T> {

    /**
     * traversal to apply on each of the sources nodes.
     */
    private Traversal<R,?,T> internalTraversal;

    /**
     *
     * @param traversal to apply on each of the sources nodes.
     */
    public MultipleSourcesTraversalImpl(Traversal<R,?,T> traversal) {

        this.internalTraversal = traversal;
    }

    /**
     *
     * @param sources to start the traversal from.
     * @return a mapping of sources->results calculated by applying {@link #internalTraversal} to each of the sources
     * nodes.
     */
    @Override
    public Map<T,R> traverse(Collection<T> sources) {

        Map<T,R> resultsMapping = new HashMap<T,R>();

        for (T source : sources) {

            final R sourceResult = this.internalTraversal.apply(source);

            resultsMapping.put(source, sourceResult);
        }

        return resultsMapping;
    }

}
