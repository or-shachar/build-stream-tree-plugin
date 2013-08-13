package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log;
import groovy.lang.GroovyClassLoader;
import jenkins.model.Jenkins;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class GroovyClassFactory<T> {

    public static final ClassLoader CONTEXT_CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    private Class<? extends T> classImpl;

    public GroovyClassFactory(Class<? extends T> classImpl) {
        this.classImpl = classImpl;
    }

    public static <T> GroovyClassFactory<T> load(Class<T> requiredType, String scriptContent) {

        final GroovyClassLoader gcl = new GroovyClassLoader(CONTEXT_CLASS_LOADER);

        try {

            Class<? extends T> classImpl = (Class<? extends T>)gcl.parseClass(scriptContent);
            return new GroovyClassFactory<T>(classImpl);
        }

        catch (Exception e) {
            Log.warning("failed to parse " + scriptContent + " for class " + requiredType.getName());
            Log.throwing(GroovyClassFactory.class.getName(), "load", e);
            return null;
        }
    }

    public T newInstance(Object initObject) {

        T instance = null;

        try {

            if (initObject == null) {
                instance = (T)classImpl.newInstance();
            }
            else {
                try {
                    instance = (T)classImpl.getConstructor(Object.class).newInstance(initObject);
                }
                //this isn't really an error, it just means that this instance doesn't need the init object,
                // even though it's defined, so they didn't make a constructor for it.
                catch(NoSuchMethodException e) {
                    instance = (T)classImpl.newInstance();
                }
            }

        }

        catch (Exception e) {
            Log.warning("failed to load class " + classImpl.getName() + " with parameter " + initObject);
            Log.throwing(GroovyClassFactory.class.getName(), "newInstance", e);
        }

        return instance;
    }
}
