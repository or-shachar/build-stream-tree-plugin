package hudson.plugins.clonejobs.view.impl;

import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.plugins.nested_view.NestedView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/2/12
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO merge with views-manager and put in jenkins-commons
 */
public class NestedViewReflectionWorkarounds {

    public static void setOwner(NestedView view, ViewGroup owner) {
        try {
            final Method setOwner = NestedView.class.getDeclaredMethod("setOwner", ViewGroup.class);
            setOwner.setAccessible(true);
            setOwner.invoke(view, owner);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to set owner via reflection for " +
                    view.getViewName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("failed to set owner via reflection for " +
                    view.getViewName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("failed to set owner via reflection for " +
                    view.getViewName(), e);
        }
    }

    public static void AddView(NestedView container, View contained) {

        try {
            final Method addView = NestedView.class.getDeclaredMethod("addView", View.class);
            addView.setAccessible(true);
            addView.invoke(container, contained);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to set add view '" + contained.getViewName() +
                    "' to '" + container.getViewName() + "' via reflection", e);
        }
    }
}
