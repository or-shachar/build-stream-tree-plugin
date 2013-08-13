package com.hp.maven.plugins;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Map;

public class XmlHelperUtil {

	
	public static void replaceTextNodeValue(
			Document doc,
			String nodesPath,
			String elementsTextValue,
			Map<String,String> prefixToNamespaceMap,
			Log log) throws XPathExpressionException
	{
		
		
		XPathFactory factoryxpath = XPathFactory.newInstance();
		XPath xpath = factoryxpath.newXPath();
	
		xpath.setNamespaceContext(PersonalNamespaceContext.getContextInstance(prefixToNamespaceMap));
		//*"//book[author='Neal Stephenson']/title/text()";
		XPathExpression expr = xpath.compile(nodesPath + XpathSyntextConsts.Text_Method);
	
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
		    String previousNodeValue = nodes.item(i).getNodeValue();
		    nodes.item(i).setTextContent(elementsTextValue);
		    log.info("Node value chagned from: '" + previousNodeValue + "' to: '" + nodes.item(i).getNodeValue() + "'"); 
		}
	}
	
	public static Document createDocument(String filePath,DocumentBuilderFactory factory) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc =  builder.parse(filePath);
		return doc;
	}
	
}
