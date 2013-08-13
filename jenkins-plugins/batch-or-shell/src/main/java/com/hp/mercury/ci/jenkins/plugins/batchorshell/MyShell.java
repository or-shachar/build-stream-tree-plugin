package com.hp.mercury.ci.jenkins.plugins.batchorshell;

import hudson.Extension;
import hudson.FilePath;
import hudson.Functions;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.remoting.Callable;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tasks.Messages;
import hudson.tasks.Shell;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/7/12
 * Time: 12:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyShell extends Shell implements MyCommandInterpreter {

    public MyShell(String command) {
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

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Extension
    public static class DescriptorImpl extends Shell.DescriptorImpl {
        /**
         * Shell executable, or null to default.
         */
        private String shell;

        public DescriptorImpl() {
            load();
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        public String getShell() {
            return shell;
        }

        /**
         *  @deprecated 1.403
         *      Use {@link #getShellOrDefault(hudson.remoting.VirtualChannel) }.
         */
        public String getShellOrDefault() {
            if(shell==null)
                return Functions.isWindows() ?"sh":"/bin/sh";
            return shell;
        }

        public String getShellOrDefault(VirtualChannel channel) {
            if (shell != null)
                return shell;

            String interpreter = null;
            try {
                interpreter = channel.call(new Shellinterpreter());
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }
            if (interpreter == null) {
                interpreter = getShellOrDefault();
            }

            return interpreter;
        }

        public void setShell(String shell) {
            this.shell = Util.fixEmptyAndTrim(shell);
            save();
        }

        public String getDisplayName() {
            return Messages.Shell_DisplayName();
        }

        @Override
        public Builder newInstance(StaplerRequest req, JSONObject data) {
            return new Shell(data.getString("command"));
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject data) {
            setShell(req.getParameter("shell"));
            return true;
        }

        /**
         * Check the existence of sh in the given location.
         */
        public FormValidation doCheck(@QueryParameter String value) {
            // Executable requires admin permission
            return FormValidation.validateExecutable(value);
        }

        private static final class Shellinterpreter implements Callable<String, IOException> {

            private static final long serialVersionUID = 1L;

            public String call() throws IOException {
                return Functions.isWindows() ? "sh" : "/bin/sh";
            }
        }

    }
}
