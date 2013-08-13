package com.hp.il.maven;

import junit.framework.TestCase;
import org.apache.maven.plugin.MojoFailureException;

import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: elimeleh
 * Date: 20/12/12
 * Time: 10:33
 * To change this template use File | Settings | File Templates.
 */
public class OverridePropertiesTest extends TestCase {

    private OverridePropertiesMojo overrideMojo;
    private final String PROPERTIES_FILE_NAME = "SourceProperties.properties";
    String tmpDir = System.getProperty("java.io.tmpdir");
   // private Properties sourceProperties;
    URL propertiesFileUrl;

    protected void setUp() throws Exception {
            overrideMojo = new OverridePropertiesMojo();
            propertiesFileUrl = this.getClass().getClassLoader().getResource(PROPERTIES_FILE_NAME);
    }


    /**
    *@Test
     * tests the creation of properties object out of a given file path
     */
    public void testCreateSourcePropertyObject() throws MojoFailureException {

            Properties sourceProperties = overrideMojo.createSourcePropertyObject(propertiesFileUrl.getPath());
            assertTrue(sourceProperties != null);
    }

    /**
     *@Test
     * tests the creation of properties object out of a given file path
     */
    public void testOverrideProperties() {

        //source properties initialization
         Properties sourceProperties = getSourceProperties();

        //override properties initialization
        Properties overrideProperties = getOverrideProperties();

        overrideMojo.setOverrideProperties(overrideProperties);

        overrideMojo.overrideProperties(sourceProperties);

        //values override assertion
        assertTrue(sourceProperties.getProperty("key1") == overrideProperties.getProperty("key1") &&
                                    sourceProperties.getProperty("key3") == overrideProperties.getProperty("key3") &&
                                    overrideProperties.getProperty("key1") == "overridden1");

    }


    /**
     *@Test
     * tests the creation of new  properties file
     */
    public void testCreateNewSourcePropertiesFile() throws MojoFailureException {

        Properties sourceProperties = getSourceProperties();
        overrideMojo.setPathToPropertiesFile(tmpDir + "/SourceProperties.properties");
        overrideMojo.createNewSourcePropertiesFile(sourceProperties);

    }


    private Properties getSourceProperties() {

        Properties sourceProperties = new Properties();
        int i = 0;

        while (i < 4){
            sourceProperties.setProperty("key" + i , "value" + i);
            i++;
        }

        return  sourceProperties;
    }


    private Properties getOverrideProperties()  {

        Properties overrideProperties = new Properties();

        overrideProperties.setProperty("key1" , "overridden1");
        overrideProperties.setProperty("key3" , "overridden3");

        return overrideProperties;

    }

}