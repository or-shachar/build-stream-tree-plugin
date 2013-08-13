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
import java.util.Map;

/**
 * replaces text in given {@link filePath} in all nodes  that fits to the xpath query {@link nodesPath} to given {@link elementTextValue}
 * @goal set-xml-elements-text
 * @phase validate
 */
public class SetXmlElementsText extends AbstractMojo{

	/**
     * the location of xml file
     *
     * @parameter expression="${file.path}"
     * @required
     */
	 private String filePath;
	
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
	  * map between prefix and name space 
	 * @parameter expression="${rep.env}"
	 * 
	 */
	 private Map<String,String> prefixToNamespaceMap = null;
	 

	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		try
		{
			
			getLog().info("execute plugin com.hp.maven.plugins.xml-helper goal: set-xml-elements-text parameters: filePath:" + filePath 
					+ " nodesPath:" + nodesPath + "elementsTextValue: "+ elementsTextValue);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			if(prefixToNamespaceMap != null && !prefixToNamespaceMap.isEmpty());
			{
				getLog().info(prefixToNamespaceMap.toString());
				factory.setNamespaceAware(true); // set namespace awareness only for xml's that includes namespace
			}
			
			Document doc=XmlHelperUtil.createDocument(filePath, factory);
			
			XmlHelperUtil.replaceTextNodeValue(doc, nodesPath, elementsTextValue, prefixToNamespaceMap, getLog());
			// save the result
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform
		        (new DOMSource(doc), new StreamResult(new File(filePath)));
		} catch (SAXException e) {
			
			throw new MojoExecutionException("problem occure:", e);
		} catch (IOException e) {
			
			throw new MojoExecutionException("problem occure:",e);
		
		} catch (ParserConfigurationException e) {
			
			throw new MojoExecutionException("problem occure:",e);
		} catch (XPathExpressionException e) {
			
			throw new MojoExecutionException("problem occure:",e);
		}
		catch (IllegalArgumentException e)
		{
			throw new MojoExecutionException("problem occure:",e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			throw new MojoExecutionException("problem occure:",e);
		}
	}

}
