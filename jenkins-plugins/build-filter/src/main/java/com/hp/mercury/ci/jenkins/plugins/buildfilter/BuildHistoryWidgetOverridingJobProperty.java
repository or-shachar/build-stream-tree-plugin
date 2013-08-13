package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.*;
import hudson.model.Queue;
import hudson.model.listeners.RunListener;
import hudson.util.RunList;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 24/06/13
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 *
 * this class is a great example of how to plan behavior in a jenkins job without having the user always
 * have to check a box.
 *
 */
public class BuildHistoryWidgetOverridingJobProperty extends JobProperty {

    private Boolean on;
    transient private BranchesCache branchesCache;

    public BranchesCache getBranchesCache() {
        if (branchesCache == null) {
            branchesCache = new BranchesCache();
        }

        return branchesCache;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    @DataBoundConstructor
    public BuildHistoryWidgetOverridingJobProperty(Boolean on) {
        this.on = on;
        this.branchesCache = new BranchesCache();
    }

    @Override
    public Collection<?> getJobOverrides() {

        try {
            Collection ret = null;

            if (isRelevant()) {

                final BranchAwareBuildHistoryWidget overridingWidget = new BranchAwareBuildHistoryWidget(
                        (Queue.Task) owner,
                        owner.getBuilds(),
                        BranchAwareBuildHistoryWidget.HISTORY_ADAPTER
                );

                ret = Collections.singleton(new Overrider(overridingWidget, owner));
            }

            return ret == null ? Collections.emptyList() : ret;
        }
        catch (Exception e) {
            Log.log(Level.WARNING, "builds-filter failed to override gui of " + owner);
            Log.LOGGER.throwing(getClass().getName(), "getJobOverrides", e);
            return Collections.emptyList();
        }
    }

    public boolean isRelevant() {

        final BuildHistoryWidgetOverridingJobProperty.DescriptorImpl descriptor =
                (BuildHistoryWidgetOverridingJobProperty.DescriptorImpl) Jenkins.getInstance().getDescriptor(
                        BuildHistoryWidgetOverridingJobProperty.class);

        return jobConfigurationContainsABranchParameterName() &&
                (descriptor.getAutoOverride() || (on != null && on));

    }

    private boolean jobConfigurationContainsABranchParameterName() {

        final BuildHistoryWidgetOverridingJobProperty.DescriptorImpl descriptor =
                (BuildHistoryWidgetOverridingJobProperty.DescriptorImpl) Jenkins.getInstance().getDescriptor(
                        BuildHistoryWidgetOverridingJobProperty.class);

        if (owner != null) {
            final ParametersDefinitionProperty parameterDefProp =
                    (ParametersDefinitionProperty)owner.getProperty(ParametersDefinitionProperty.class);

            if (parameterDefProp != null) {
                for (ParameterDefinition paramDef : parameterDefProp.getParameterDefinitions()) {
                    if (paramDef instanceof StringParameterDefinition) {
                        StringParameterDefinition spd = (StringParameterDefinition) paramDef;
                        for (String branchParameterName : descriptor.getBranchParameterNamesAsStrings()) {
                            if (branchParameterName.equals(spd.getName())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {

        private Boolean showBranchBadges;
        private Boolean autoOverride;

        private Boolean ignoreHashes;
        private List<StringWrapper> repositoryParameterNames;
        private Boolean initialized;

        public DescriptorImpl() {
            load();
            assertDefaults();
        }

        private void assertDefaults() {

            if (this.branchParameterNames == null) {
                this.branchParameterNames = Arrays.asList(
                        new StringWrapper("BRANCH"),
                        new StringWrapper("GIT_REVISION"),
                        new StringWrapper("REVISION"));
            }

            if (this.repositoryParameterNames == null) {
                this.repositoryParameterNames = Arrays.asList(
                        new StringWrapper("CODE_REPOSITORY"),
                        new StringWrapper("GIT_REPOSITORY"),
                        new StringWrapper("GIT_REPOSITORY_URL")
                );
            }

            if (showBranchBadges == null) {
                showBranchBadges = true;
            }

            if (autoOverride == null) {
                autoOverride = true;
            }

            if (ignoreHashes == null) {
                ignoreHashes = true;
            }

            if (initialized == null) {
                initialized = false;
            }
        }

        @Override
        public String getDisplayName() {
            return "override build history";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }

        protected List<StringWrapper> branchParameterNames;

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            super.configure(req, json);

            JSONObject submittedForm;
            try {
                 submittedForm = req.getSubmittedForm();
            } catch (ServletException e) {
                Log.log(Level.WARNING, "failed to configure builds-filter.");
                Log.LOGGER.throwing(getClass().getName(), "configure", e);
                submittedForm = null;
            }

            if (submittedForm != null) {

                branchParameterNames = new ArrayList<StringWrapper>();
                for (Object branchParameterName : submittedForm.getJSONArray("branchParameterNames")) {
                    JSONObject container = (JSONObject)branchParameterName;
                    branchParameterNames.add(new StringWrapper(container.getString("string")));
                }

                repositoryParameterNames = new ArrayList<StringWrapper>();
                for (Object branchParameterName : submittedForm.getJSONArray("repositoryParameterNames")) {
                    JSONObject container = (JSONObject)branchParameterName;
                    repositoryParameterNames.add(new StringWrapper(container.getString("string")));
                }

                showBranchBadges = submittedForm.getBoolean("showBranchBadges");
                autoOverride = submittedForm.getBoolean("autoOverride");
                ignoreHashes = submittedForm.getBoolean("ignoreHashes");

                save();
            }

            return true;
        }

        public Boolean getIgnoreHashes() {
            return ignoreHashes;
        }

        public List<StringWrapper> getBranchParameterNames() {
            return branchParameterNames;
        }

        public Boolean getShowBranchBadges() {
            return showBranchBadges;
        }

        public boolean getAutoOverride() {
            return autoOverride;
        }

        public List<String> getBranchParameterNamesAsStrings() {
            return stringWrapperListToStringList(getBranchParameterNames());
        }

        private List<String> stringWrapperListToStringList(List<StringWrapper> stringWrappers) {
            List<String> ret = new ArrayList(stringWrappers.size());
            for (StringWrapper b : stringWrappers) {
                ret.add(b.getString());
            }
            return ret;
        }

        public List<String> getRepositoryParameterNamesAsStrings() {
            return stringWrapperListToStringList(getRepositoryParameterNames());
        }

        public List<StringWrapper> getRepositoryParameterNames() {
            return repositoryParameterNames;
        }

        public Boolean isInitialized() {
            return initialized;
        }

        public void setInitialized(Boolean initialized) {
            this.initialized = initialized;
        }
    }

    @Initializer(after = InitMilestone.JOB_LOADED)
    public static void init() throws Exception {

        final DescriptorImpl descriptor = (DescriptorImpl)
                (Jenkins.getInstance().getDescriptor(BuildHistoryWidgetOverridingJobProperty.class));
        if (!(descriptor.isInitialized())) {

            for (Job job : Jenkins.getInstance().getItems(Job.class)) {
                if (job.getProperty(BuildHistoryWidgetOverridingJobProperty.class) == null) {
                    try {
                        job.addProperty(new BuildHistoryWidgetOverridingJobProperty(false));
                    } catch (IOException e) {
                        Log.log(Level.WARNING, "failed to add auto-override job property to job " + job.getFullDisplayName());
                        Log.LOGGER.throwing(BuildHistoryWidgetOverridingJobProperty.class.getName(), "init", e);
                    }
                }
            }
        }
        descriptor.setInitialized(true);
    }

    private class BranchesCache {

        private Boolean ignoreHashes;
        private List<String> branchParameterNames;
        private List<String> repositoryParameterNames;
        transient private Boolean initialized;

        public Boolean getInitialized() {
            return initialized;
        }

        public void setInitialized(Boolean initialized) {
            this.initialized = initialized;
        }

        public BranchesCache() {
            this.initialized = false;
        }

        //branch name -> number of builds belonging to that branch
        private Map<String, Integer> map = new HashMap<String,Integer>();

        public synchronized Set<String> getBranches() {
            assertValidity();
            return Collections.unmodifiableSet(map.keySet());
        }

        public synchronized void removeBranchReference(String branchName) {
            final Integer branchReferencesCount = getBranchReferencesCount(branchName);
            if (branchReferencesCount == 1) {
                map.remove(branchName);
            }
            else {
                map.put(branchName, branchReferencesCount - 1);
            }
        }

        public synchronized void addBranchReference(String branchName) {

            map.put(branchName,getBranchReferencesCount(branchName)+1);
        }

        public synchronized Integer getBranchReferencesCount(String branchName) {
            final Integer referencesCount = map.get(branchName);
            return referencesCount == null ? 0 : referencesCount;
        }

        private synchronized void assertValidity() {
            if (!valid()) rebuild();
        }

        private synchronized void rebuild() {

            final DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptor(
                    BuildHistoryWidgetOverridingJobProperty.class);

            this.ignoreHashes = descriptor.getIgnoreHashes();
            this.branchParameterNames = Collections.unmodifiableList(descriptor.getBranchParameterNamesAsStrings());
            this.repositoryParameterNames = Collections.unmodifiableList(descriptor.getRepositoryParameterNamesAsStrings());

            map.clear();

            final RunList<Run> builds = owner.getBuilds();

            for (Run r : builds) {
                final String runBranch = BranchAwareBuildHistoryWidget.getRunBranch(r);
                if (runBranch != null) {
                    addBranchReference(runBranch);
                }
            }

            this.initialized = true;
        }

        private synchronized boolean valid() {
            if (initialized == null || !initialized) {
                return false;
            }

            final DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptor(
                    BuildHistoryWidgetOverridingJobProperty.class);

            return descriptor.getIgnoreHashes().equals(this.ignoreHashes) &&
                    descriptor.getBranchParameterNamesAsStrings().equals(this.branchParameterNames) &&
                    descriptor.getRepositoryParameterNamesAsStrings().equals(this.repositoryParameterNames);

        }

    }

    @Extension
    public static class JobBranchesCacheUpdatingRunListener extends RunListener<Run> {

        @Override
        public void onStarted(Run run, TaskListener listener) {
            super.onStarted(run, listener);

            final String branch = BranchAwareBuildHistoryWidget.getRunBranch(run);

            if (branch != null) {
                listener.getLogger().println("adding branch parameter value to cache: " + branch);
                final BuildHistoryWidgetOverridingJobProperty property = (BuildHistoryWidgetOverridingJobProperty) run.getParent().getProperty(
                        BuildHistoryWidgetOverridingJobProperty.class);
                if (property != null) {
                    property.getBranchesCache().addBranchReference(branch);
                }
            }
        }

        @Override
        public void onDeleted(Run run) {
            super.onDeleted(run);

            final String branch = BranchAwareBuildHistoryWidget.getRunBranch(run);

            if (branch != null) {
                ((BuildHistoryWidgetOverridingJobProperty)run.getParent().getProperty(
                        BuildHistoryWidgetOverridingJobProperty.class)).getBranchesCache().removeBranchReference(branch);
            }
        }
    }
}
