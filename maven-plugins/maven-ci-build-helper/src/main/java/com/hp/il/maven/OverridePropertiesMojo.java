package com.hp.il.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: elimeleh
 * Date: 19/12/12
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */

/**
 * @goal overrideProperties
 * @phase pre-integration-test
 *  @requiresDependencyResolution test
 */
public class OverridePropertiesMojo extends AbstractMojo {

    /**
     * @parameter expression="${pathToPropertiesFile}"
     * @required
     */
    private String pathToPropertiesFile;

    /**
     * @parameter expression="${overrideProperties}"
     * @required
     */
    private Properties overrideProperties;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

            // pathToPropertiesFile parameter is missing
            if (pathToPropertiesFile == null){

                throw new MojoFailureException("properties file is null: if you run the mojo via command line, pass the parameter with -DpathToPropertiesFile=value. if you run the mojo via pom xml profile, make sure the configuration tag contains the parameter and its valid value.");

            }
            else{
                        getLog().info("overrideProperties is: "+ overrideProperties);
                        if(overrideProperties.isEmpty()){
                                throw new MojoFailureException("No Override properties defined: if you run the mojo via command line, pass the parameter with -DoverrideProperties={key=value, key=value...}. if you run the mojo via pom xml profile, make sure the configuration tag contains the parameter and its valid value.");
                        }
            }

            Properties sourceProperties =  createSourcePropertyObject(pathToPropertiesFile );
            overrideProperties(sourceProperties);

            createNewSourcePropertiesFile(sourceProperties);

    }


    //creates properties object out of the given file
    public Properties createSourcePropertyObject(String pathToPropertiesFile) throws MojoFailureException {

        //creates input stream
        File propertiesFile = new File(pathToPropertiesFile);
        InputStream propertiesInput;
        try {
            propertiesInput = new FileInputStream (propertiesFile);
        } catch (FileNotFoundException e) {
            throw new MojoFailureException("Failed to find the file in: "
                    + pathToPropertiesFile);
        }
        //creates properties
        Properties sourceProperties = new Properties();
        try {
            sourceProperties.load(propertiesInput);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoFailureException("Failed to load properties of given file: " + pathToPropertiesFile ) ;
        }

        return sourceProperties;
    }



    //overrides source properties with the given overrideProperties properties
    public void overrideProperties(Properties sourceProperties) {
         for(String overrideProperty: overrideProperties.stringPropertyNames()){
                   String overrideValue = overrideProperties.getProperty(overrideProperty);
                   if (sourceProperties.getProperty(overrideProperty)!=null){
                            sourceProperties.setProperty(overrideProperty,overrideValue);
                   }
         }
    }

    public void createNewSourcePropertiesFile(Properties sourceProperties) throws MojoFailureException {

            File newFile = new File(pathToPropertiesFile);
            OutputStream out;

            try {
                    out = new FileOutputStream(newFile);
            } catch (FileNotFoundException e) {
                    throw new MojoFailureException("Failed to create new file", e);
            }
        try {
            sourceProperties.store(out , "This file was partially overridden by OverridePropertiesMojo");
        } catch (IOException e) {
            throw new MojoFailureException("Failed to store properties to a new file", e);
        }

    }

    public void setOverrideProperties(Properties properties){

               this.overrideProperties =  properties;
    }

    public void setPathToPropertiesFile(String pathToFile){

        this.pathToPropertiesFile = pathToFile;
    }


    }