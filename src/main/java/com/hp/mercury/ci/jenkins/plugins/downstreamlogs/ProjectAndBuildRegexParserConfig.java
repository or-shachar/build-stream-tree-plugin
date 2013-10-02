package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 30/05/13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public class ProjectAndBuildRegexParserConfig implements Describable<ProjectAndBuildRegexParserConfig> {

    private String groovyRegex;
    transient private Pattern regexPattern;
    private RegexParseOrder regexParseOrder;

    //private static DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public Descriptor<ProjectAndBuildRegexParserConfig> getDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    public enum RegexParseOrder {
        PROJECT_ONLY,
        PROJECT_FIRST_BUILD_SECOND,
        BUILD_FIRST_PROJECT_SECOND
    }

    @DataBoundConstructor
    public ProjectAndBuildRegexParserConfig(String groovyRegex, Integer regexParseOrderEnumOrdinal) {
        this.groovyRegex = groovyRegex;
        this.regexParseOrder = RegexParseOrder.values()[regexParseOrderEnumOrdinal];
    }

    public String getGroovyRegex() {
        return groovyRegex;
    }

    public void setGroovyRegex(String groovyRegex) {
        this.groovyRegex = groovyRegex;
    }

    public RegexParseOrder getRegexParseOrder() {
        return regexParseOrder;
    }

    public void setRegexParseOrder(Integer regexParseOrderEnumOrdinal) {
        this.regexParseOrder = RegexParseOrder.values()[regexParseOrderEnumOrdinal];
    }

    public Integer getProjectIndex() {
        return (this.regexParseOrder == RegexParseOrder.BUILD_FIRST_PROJECT_SECOND) ?
                2 :
                1;
    }

    public Integer getBuildIndex() {
        return (this.regexParseOrder == RegexParseOrder.PROJECT_ONLY) ?
                null :
                //value is 1 if 2 and 2 if 1..
                3 - getProjectIndex();
    }

    public Pattern getRegexPattern() {
        if (this.regexPattern == null) {
            this.regexPattern = Pattern.compile(this.groovyRegex);
        }
        return this.regexPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectAndBuildRegexParserConfig)) return false;

        ProjectAndBuildRegexParserConfig that = (ProjectAndBuildRegexParserConfig) o;

        if (!groovyRegex.equals(that.groovyRegex)) return false;
        if (regexParseOrder != that.regexParseOrder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groovyRegex.hashCode();
        result = 31 * result + regexParseOrder.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProjectAndBuildRegexParserConfig{" +
                "groovyRegex='" + groovyRegex + '\'' +
                ", regexParseOrder=" + regexParseOrder +
                '}';
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ProjectAndBuildRegexParserConfig> {

        @Override
        public String getDisplayName() {
            return "Project and build regex parser config";
        }
    }
}
