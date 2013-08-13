package com.hp.il.maven;

import com.hp.commons.core.unique.UniquenessUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.util.Properties;


/**
 * Created with IntelliJ IDEA.
 * User: elimeleh
 * Date: 05/12/12
 * Time: 10:54
 * This mojo generates 30-chars schemaName and saves it as system property named "SchemaName" or schemaPropertyName if provided .
 */
/**
 * @goal generateSchemaName
 * @phase pre-integration-test
 *  @requiresDependencyResolution test
 */

public class GenerateSchemaNameMojo extends AbstractMojo {


    /**
     * @parameter expression="${prefix}"
     */
    private String prefix;

    /**
     * @parameter
     * expression="${schemaNameLength}"
     * default-value=30
     */
    private int schemaNameLength;

    /**
     * @parameter
     * expression="${generatedUniqueIdLength}"
     * default-value=10
     */
    private int generatedUniqueIdLength;

    /**
     * @parameter
     * expression="${schemaPropertyName}"
     * default-value="SchemaName"
     */
    private String schemaPropertyName;

    /**
     * @parameter
     * expression="${project}"
     */
    protected MavenProject project;

    //schema name to return
    private String schemaName;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

            //prefix parameter was not given, set value to an empty string
            if (prefix==null || prefix == ""){
                getLog().info("Prefix parameter was not defined,  prefix  is an empty string");
                prefix="";
                generatedUniqueIdLength = schemaNameLength;
            }

            else{
                    //given prefix is too long (more than 20 chars, should be cut
                    if (prefix.length() > (schemaNameLength- generatedUniqueIdLength)) {
                                String oldPrefix = prefix;
                                prefix = prefix.substring( 0 , schemaNameLength- generatedUniqueIdLength -1 ) + "_";
                                getLog().debug("prefix is longer than 20 chars");
                                getLog().info("Prefix parameter " + oldPrefix +" is too long, will be truncated to: " + prefix);
                    }

                    else{
                            //given prefix is too small, random suffix should be longer
                            if (prefix.length() <= (schemaNameLength- generatedUniqueIdLength)) {

                                generatedUniqueIdLength = schemaNameLength - prefix.length()-1;
                                prefix = prefix + "_";
                                getLog().debug("in second if: random sequence should be larger than 10 chars");
                            }
                    }
            }

            getLog().debug("prefix is:" + prefix);
            getLog().debug("schemaNameLength is:" + schemaNameLength);
            getLog().debug("generatedUniqueId is:" + generatedUniqueIdLength);

            schemaName = prefix +  UniquenessUtils.randomAlphanumeric(generatedUniqueIdLength);

        if (project!=null){
            getLog().info("project name:  "+ project.getName());

            //set generated schema name as maven project properties
            Properties properties = project.getProperties();
            properties.put( schemaPropertyName , schemaName );

        }

        getLog().info("Generated schema name: "+ schemaName);
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSchemaNameLength(int schemaNameLength) {
        this.schemaNameLength = schemaNameLength;
    }

    public void setGeneratedUniqueIdLength(int generatedUniqueId) {
        this.generatedUniqueIdLength = generatedUniqueId;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public int getGeneratedUniqueIdLength() {
        return this.generatedUniqueIdLength;
    }

    public String getPrefix() {
        return this.prefix;
    }

}