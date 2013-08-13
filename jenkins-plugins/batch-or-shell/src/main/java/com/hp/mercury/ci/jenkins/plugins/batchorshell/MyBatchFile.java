package com.hp.mercury.ci.jenkins.plugins.batchorshell;

import hudson.FilePath;
import hudson.tasks.BatchFile;

public class MyBatchFile extends BatchFile implements MyCommandInterpreter {

    public MyBatchFile(String command) {
        super(command);
    }

    @Override
    public String[] buildCommandLine(FilePath script) {
        return super.buildCommandLine(script);
    }

    @Override
    public String getContents() {
        return super.getContents();
    }

    @Override
    public String getFileExtension() {
        return super.getFileExtension();
    }
}
