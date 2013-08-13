package com.hp.mercury.ci.jenkins.plugins.batchorshell;

import hudson.FilePath;
import hudson.tasks.BatchFile;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/7/12
 * Time: 12:52 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MyCommandInterpreter {

    public String[] buildCommandLine(FilePath script);

    public String getContents();

    public String getFileExtension();
}

