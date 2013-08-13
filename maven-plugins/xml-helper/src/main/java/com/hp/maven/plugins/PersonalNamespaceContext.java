package com.hp.maven.plugins;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.Map;

public class PersonalNamespaceContext implements NamespaceContext {

	private Map<String,String> prefixToNameSpace;
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
        	throw new NullPointerException("No Namespace mapped to prefix: "+ prefix);
        }
        else if (prefixToNameSpace.containsKey(prefix))
        {
        	return prefixToNameSpace.get(prefix);
        }

        return XMLConstants.NULL_NS_URI;
    }

    private PersonalNamespaceContext(Map<String,String> nameSpace)
    {
    	super();
    	this.prefixToNameSpace = nameSpace;  	
    }
    
    
    public static PersonalNamespaceContext getContextInstance(Map<String,String> nameSpace)
    {
    	return new PersonalNamespaceContext(nameSpace);
    }
    
    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

}
