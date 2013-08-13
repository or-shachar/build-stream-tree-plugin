package com.hp.mercury.ci.jenkins.plugins.batchorshell;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.tasks.*;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/6/12
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchOrShell extends CommandInterpreter {

    private String batch;
    private String shell;
    private boolean same;

    private MyCommandInterpreter delegated;

    @DataBoundConstructor
    public BatchOrShell(String batch, boolean same, String shell) {

        //we won't be using the "command" field from super, and it's only used by implementation (this) so no problem.
        super(null);

        this.batch = batch;
        this.shell = shell;
        this.same = same;

    }

    @Override
    public boolean prebuild(Build build, BuildListener listener) {

        String OS = null;

        try {
            OS = build.getBuiltOn().toComputer().getSystemProperties().get("os.name").toString().toLowerCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (OS.contains("win")) {
            this.delegated = new MyBatchFile(batch);
        }
        else {
            this.delegated = new MyShell(this.same ? batch : shell);
        }

        return super.prebuild(build, listener);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String[] buildCommandLine(FilePath script) {

        return this.delegated.buildCommandLine(script);
    }

    @Override
    protected String getContents() {
        return this.delegated.getContents();
    }

    @Override
    protected String getFileExtension() {
        return this.delegated.getFileExtension();
    }

    public String getBatch() {
        return batch;
    }

    public String getShell() {
        return shell;
    }

    public boolean isSame() {
        return same;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Execute Command Line Appropriate To OS";
        }
    }
}
