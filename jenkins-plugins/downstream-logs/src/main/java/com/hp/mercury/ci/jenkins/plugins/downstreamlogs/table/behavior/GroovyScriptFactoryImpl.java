package com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.behavior;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.Log;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 07/07/13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class GroovyScriptFactoryImpl<T> extends GroovyScriptFactory<T> {

    private Class<? extends T> classImpl;

    public GroovyScriptFactoryImpl(Class<? extends T> classImpl) {
        this.classImpl = classImpl;
    }

    //this is the content.groovy script
    public T newInstance(Script initObject) {

        T instance = null;

        try {

            final Binding context = new Binding();
            context.setVariable("content.groovy", initObject);
            instance =
                    (T)InvokerHelper.createScript(classImpl, context).run();
        }

        catch (Exception e) {
            Log.warning("failed to load script " + classImpl.getName() + " with parameter " + initObject);
            Log.throwing(GroovyScriptFactoryImpl.class.getName(), "newInstance", e);
        }

        return instance;
    }


}
