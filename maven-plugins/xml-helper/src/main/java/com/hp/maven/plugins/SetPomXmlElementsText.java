package com.hp.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * replaces text in given {@link filePath} in all nodes  that fits to the xpath query {@link nodesPath} to given {@link elementTextValue}
 * @goal set-pom-xml-elements-text
 * @phase validate
 */
public class SetPomXmlElementsText  extends AbstractMojo{

	 /**
	  * nodes path written in xpath standard syntax see {@link http://www.w3schools.com/xpath/xpath_syntax.asp}
	  * @parameter expression="${node.path}"
	  * @required
	  */
	 private String nodesPath;
	 
	 /**
	  * new value that will be put in element text
	  * @parameter expression="${text.value}"
	  * @required
	  */
	 private String elementsTextValue;
	
	 
	 /**
	 * the location of xml file relative to where relative to current position.
	 * if no directory given take pom.xml  file from  current directory 
	 * @parameter expression="${pom.directory.path}"
	 * 
	 */
	 private String pomDirectoryPath;
	 
	 private Map<String,String> prefixToNamespaceMap;
	 
	 private static final String Pom_Xml="pom.xml";
	 
	 private static final String Default_NameSpace= "defaultnamespace";
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
	   
		initPomXmlNameSpace(); 
		try
		{
		   
			
			String pomFilePath;
			if(pomDirectoryPath==null || pomDirectoryPath=="")
			{
				
				pomFilePath=Pom_Xml;
			}
			else
			{
				pomFilePath=pomDirectoryPath+"/"+Pom_Xml;
			}
			
			getLog().info("execute plugin com.hp.maven.plugins.xml-helper goal: set-pom-xml-elements-text " +
					"parameters:"
					+ " nodesPath:" + nodesPath + "elementsTextValue: "+ elementsTextValue);
			
			getLog().info("reading  pom  file :" + pomFilePath);
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			getLog().info(prefixToNamespaceMap.toString());
			factory.setNamespaceAware(true); // set namespace awareness only for xml's that includes namespace
			getLog().info(prefixToNamespaceMap.toString());
			
			Document doc=XmlHelperUtil.createDocument(pomFilePath, factory);
			XmlHelperUtil.replaceTextNodeValue(doc, nodesPath, elementsTextValue, prefixToNamespaceMap, getLog());
			
			// save the result
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform
		        (new DOMSource(doc), new StreamResult(new File(pomFilePath)));
		
		} catch (SAXException e) {
		
			throw new MojoExecutionException("problem occur: ", e);
		} catch (IOException e) {
		
			throw new MojoExecutionException("problem accur: ",e);
	
		} catch (ParserConfigurationException e) {
		
			throw new MojoExecutionException("problem accur: ",e);
		} catch (XPathExpressionException e) {
		
		throw new MojoExecutionException("problem accur: ",e);
		}
		catch (IllegalArgumentException e)
		{
			throw new MojoExecutionException("problem occur: ",e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			throw new MojoExecutionException("problem occur: ",e);
		}
		
	}
	
	private void initPomXmlNameSpace()
	{
		prefixToNamespaceMap =  new HashMap<String,String>();
		prefixToNamespaceMap.put(Default_NameSpace, "http://maven.apache.org/POM/4.0.0");
	}
	

}
