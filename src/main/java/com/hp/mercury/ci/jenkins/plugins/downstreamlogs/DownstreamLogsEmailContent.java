package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import groovy.lang.Binding;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.plugins.emailext.plugins.EmailToken;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 17/06/13
 * Time: 01:48
 * To change this template use File | Settings | File Templates.
 */
@EmailToken
public class DownstreamLogsEmailContent extends DataBoundTokenMacro {

    public static String PATH = DownstreamLogsSideMenuLink.class.getName().replace('.','/') + "/content.groovy";

    @Parameter
    public String tableName;

    @Override
    public String evaluate(AbstractBuild<?, ?> abstractBuild, TaskListener taskListener, String s) throws MacroEvaluationException, IOException, InterruptedException {

        final Binding binding = new Binding();
        final DownstreamLogsSideMenuLink downstreamLogsLink = new DownstreamLogsSideMenuLink(abstractBuild);
        binding.setProperty("my", downstreamLogsLink);

        binding.setVariable("emailMode", "true");
        if (tableName != null) {
            binding.setVariable("tableName", tableName);
        }
        Class script = DownstreamLogsUtils.groovySourceToScript(DownstreamLogsSideMenuLink.class.getClassLoader(), PATH);
        return DownstreamLogsUtils.collectStringFromGroovyExecution(script, binding);
    }



    @Override
    public boolean acceptsMacroName(String s) {
        return "BUILD_STREAM_TREE".equals(s);
    }
}
