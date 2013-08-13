package hudson.plugins.clonejobs.view.impl;

import com.hp.commons.core.graph.propagator.Propagator;
import hudson.model.View;
import hudson.plugins.nested_view.NestedView;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/25/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 *
 * propagator for nested views: given a view, return all of it's sub-views.
 *
 */
public class NestedViewPropagator implements Propagator<View> {

    @Override
    public Collection<View> propagate(View view) {

        //TODO instanceof?
        if (NestedView.class.isAssignableFrom(view.getClass())) {

            NestedView nv = (NestedView)view;
            return nv.getViews();
        }

        return Collections.emptyList();
    }
}
