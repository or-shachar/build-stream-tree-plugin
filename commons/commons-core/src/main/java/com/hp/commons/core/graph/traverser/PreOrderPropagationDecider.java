package com.hp.commons.core.graph.traverser;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/3/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 *
 * TODO this class is totally redundant, remove it...
 *
 */
@Deprecated
public interface PreOrderPropagationDecider<T, POC> {

    public boolean propagate(List<Map.Entry<T, POC>> traversalResults);
}
