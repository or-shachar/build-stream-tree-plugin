package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class GroovyScriptFactory<T> {

    public abstract T newInstance(Script initObject);

    public static GroovyScriptFactory nullFactoryInstance =

            new GroovyScriptFactory() {

                @Override
                public Object newInstance(Script initObject) {
                    return null;
                }
            };

    public static GroovyScriptFactory nullFactory() {
        return nullFactoryInstance;
    }

    public static <T> GroovyScriptFactory<T> loadScript(Class<T> requiredType, String scriptContent) {

        final GroovyClassLoader gcl = new GroovyClassLoader(
                Thread.currentThread().getContextClassLoader());

        try {

            Class<? extends T> classImpl = (Class<? extends T>)gcl.parseClass(scriptContent);
            return new GroovyScriptFactoryImpl<T>(classImpl);
        }

        catch (Exception e) {
            Log.warning("failed to parse " + scriptContent + " for class " + requiredType.getName());
            Log.throwing(GroovyScriptFactoryImpl.class.getName(), "load", e);
            return null;
        }
    }
}
