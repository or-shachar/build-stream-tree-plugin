package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.Column;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.Table;
import hudson.Extension;
import hudson.model.*;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.ProjectAndBuildRegexParserConfig.RegexParseOrder;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 25/03/13
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 *
 * this is used to embed in main view of a build
 */
public class DownstreamLogsAction implements Action, Describable<DownstreamLogsAction> {

    //deprecated, backwards compatible
    private Build build;
    //it's ok to store the run instance, because this is serialized in the context of that run instance, so this is just
    // a reference.
    private Run run;

    public DownstreamLogsAction(Run build) {
        this.run = build;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return "Embedded Build Stream Tree";
    }

    public String getUrlName() {
        return "embeddedDownstreamLogs";
    }

    public Run getBuild() {

        //backwards compatability
        if (run == null && build != null) {
            run = build;
        }

        DownstreamLogsManualEmebedViaJobProperty property =
                ((DownstreamLogsManualEmebedViaJobProperty)(run.getParent().getProperty(DownstreamLogsManualEmebedViaJobProperty.class)));

        final boolean embed = (property != null && property.getOverrideGlobalConfig()) ?
                property.getEmbedInBuild() :
                getDescriptor().getAlwaysEmbedBuildTree();

        return embed ?
                run :
                null;
    }

    //private static DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public DescriptorImpl getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    public static DescriptorImpl getDescriptorStatically() {
        return Jenkins.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DownstreamLogsAction> {

        private List<ProjectAndBuildRegexParserConfig> parserConfigs;
        private List<Table> guiConfigurationTables;

        private Boolean alwaysEmbedBuildTree;
        private Boolean cacheBuilds;
        private Boolean alwaysEmbedBuildTreeInJob;

        private Boolean initialized;
        private Boolean debugMode;
        private String emailTable;

        private Map<User, String> userChosenConfig;
        public String getUserGuiTableName(User u) {
            if (userChosenConfig == null) {
                userChosenConfig = new HashMap<User,String>();
            }
            return userChosenConfig.get(u);
        }

        public void setUserGuiTableName(User u, String tableName) {
            if (userChosenConfig == null) {
                userChosenConfig = new HashMap<User,String>();
            }
            userChosenConfig.put(u, tableName);
        }


        public String getEmailTable() {
            return emailTable;
        }

        public void setEmailTable(String emailTable) {
            this.emailTable = emailTable;
        }

        public Boolean getAlwaysEmbedBuildTreeInJob() {
            return alwaysEmbedBuildTreeInJob;
        }

        public void setAlwaysEmbedBuildTreeInJob(Boolean alwaysEmbedBuildTreeInJob) {
            this.alwaysEmbedBuildTreeInJob = alwaysEmbedBuildTreeInJob;
        }

        public Boolean getInitialized() {
            return initialized;
        }

        public void setInitialized(Boolean initialized) {
            this.initialized = initialized;
        }

        public Boolean getCacheBuilds() {
            return cacheBuilds;
        }

        public void setCacheBuilds(boolean cacheBuilds) {
            this.cacheBuilds = cacheBuilds;
        }

        public Boolean getAlwaysEmbedBuildTree() {
            return alwaysEmbedBuildTree;
        }

        public void setAlwaysEmbedBuildTree(Boolean alwaysEmbedBuildTree) {
            this.alwaysEmbedBuildTree = alwaysEmbedBuildTree;
        }

        public List<ProjectAndBuildRegexParserConfig> getParserConfigs() {
            return parserConfigs;
        }

        public void setParserConfigs(List<ProjectAndBuildRegexParserConfig> parserConfigs) {
            this.parserConfigs = parserConfigs;
        }

        public void setCacheBuilds(Boolean cacheBuilds) {
            this.cacheBuilds = cacheBuilds;
        }

        public void setDebugMode(Boolean debugMode) {
            this.debugMode = debugMode;
        }

        public DescriptorImpl() {
            load();
            assertDefaults();
        }

        @Override
        public String getDisplayName() {
            return "Build Stream Tree Configurations";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {

            final JSONArray parserConfigs = json.getJSONArray("parserConfigs");
            alwaysEmbedBuildTree = json.getBoolean("alwaysEmbedBuildTree");
            alwaysEmbedBuildTreeInJob = json.getBoolean("alwaysEmbedBuildTreeInJob");
            cacheBuilds = json.getBoolean("cacheBuilds");

            debugMode = json.getBoolean("debugMode");
            Log.updateLogger();

            this.parserConfigs = new ArrayList<ProjectAndBuildRegexParserConfig>(parserConfigs.size());

            for (Object potentialParserConfig : parserConfigs) {
                JSONObject parserConfig = (JSONObject)potentialParserConfig;
                this.parserConfigs.add(
                        new ProjectAndBuildRegexParserConfig(
                                parserConfig.getString("groovyRegex"),
                                parserConfig.getInt("regexParseOrder")));
            }

            this.guiConfigurationTables =
                    req.bindJSONToList(Table.class, json.get("guiConfigurationTables"));
            if (guiConfigurationTables.isEmpty()) {
                this.guiConfigurationTables = defaultGuiTableConfigurations();
            }

            emailTable = json.getString("emailTable");
            if (emailTable == null || emailTable.isEmpty()) {
                emailTable = guiConfigurationTables.get(0).getName();
            }

            save();
            return true;
        }

        private void assertDefaults() {

            if (parserConfigs == null) {
                parserConfigs = Arrays.asList(
                        new ProjectAndBuildRegexParserConfig("Waiting for the completion of (\\S*)",
                                RegexParseOrder.PROJECT_ONLY.ordinal()),
                        //JENKINS HAS A BUG - THE BUILD NUMBER VALUE IS NOT ALWAYS CORRECT in downstream invocations.
                        new ProjectAndBuildRegexParserConfig("Triggering a new build of (\\S*) #([0-9]+)",
                                RegexParseOrder.PROJECT_ONLY.ordinal()),
                        new ProjectAndBuildRegexParserConfig("Triggering projects: (.*)",
                                RegexParseOrder.PROJECT_ONLY.ordinal()),
                        new ProjectAndBuildRegexParserConfig("Starting build job ([a-zA-Z0-9\\-]*)\\.",
                                RegexParseOrder.PROJECT_ONLY.ordinal())
                );
            }

            if (debugMode == null) {
                debugMode = true;
            }

            if (alwaysEmbedBuildTree == null) {
                alwaysEmbedBuildTree = false;
            }

            if (alwaysEmbedBuildTreeInJob == null) {
                alwaysEmbedBuildTreeInJob = false;
            }

            if (cacheBuilds == null) {
                cacheBuilds = true;
            }

            if (initialized == null) {
                initialized = false;
            }

            if (guiConfigurationTables == null) {
                guiConfigurationTables = defaultGuiTableConfigurations();
            }

            if (emailTable == null) {
                emailTable = guiConfigurationTables.get(0).getName();
            }
        }

        //TODO this is a mock, implement GUI with a good default...
        private List<Table> defaultGuiTableConfigurations() {
            return Collections.<Table>singletonList(new Table(
                    "Default",
                    Arrays.asList(

                            new Column(
                                    "+/-",
                                    new StringProvider.DefaultStringProvider("SubtreeCollapserColumnRenderer.groovy"),
                                    new StringProvider.DefaultStringProvider("SubtreeCollapserColumnRenderer.js"),
                                    new StringProvider.NoneStringProvider()),

                            createDefaultColumnConfig("#",          "RowCounterColumnRenderer"),
                            createDefaultColumnConfig("Status",     "BallStatusColumnRenderer"),
                            createDefaultColumnConfig("Problems",   "FailingLeavesColumnRenderer"),
                            createDefaultColumnConfig("Tree",       "TreeColumnRenderer"),
                            createDefaultColumnConfig("Console",    "ConsoleLinksColumnRenderer"),
                            createDefaultColumnConfig("Tests",      "TestResultsLinksColumnRenderer"),
                            createDefaultColumnConfig("Summary",    "SummaryColumnRenderer"),
                            createDefaultColumnConfig("ETA",        "ETAColumnRenderer"),
                            createDefaultColumnConfig("Badges",     "BuildBadgesColumnRenderer"),
                            createDefaultColumnConfig("Duration",   "DurationColumnRenderer"),
                            createDefaultColumnConfig("Success",    "LastSuccessColumnRenderer"),
                            createDefaultColumnConfig("Started",    "StartedOnColumnRenderer")
                    ),



                    new StringProvider.DefaultStringProvider("AlignTHLeft.css"),
                    new StringProvider.NoneStringProvider(),
                    new StringProvider.DefaultStringProvider("DefaultScriptInitializer.groovy"),
                    new StringProvider.NoneStringProvider(),
                    new StringProvider.DefaultStringProvider("MatrixSizeOneBuildEntryFilter.groovy")));
        }

        private Column createDefaultColumnConfig(String name, String className) {
            return new Column(
                    name,
                    new StringProvider.DefaultStringProvider(className + ".groovy"),
                    new StringProvider.NoneStringProvider(),
                    new StringProvider.NoneStringProvider());
        }

        public List<Table> getGuiConfigurationTables() {
            return guiConfigurationTables;
        }

        public void setGuiConfigurationTables(List<Table> guiConfigurationTables) {
            this.guiConfigurationTables = guiConfigurationTables;
        }

        public Boolean getDebugMode() {
            return debugMode;
        }
    }
}
