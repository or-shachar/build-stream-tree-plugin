package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;
import groovy.xml.QName;
import org.apache.commons.jelly.XMLOutput;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 18/06/13
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class JenkinsLikeXmlHelper {

    private final AttributesImpl attributes = new AttributesImpl();
    private XMLOutput output;

    public XMLOutput getOutput() {
        return output;
    }

    public void setOutput(XMLOutput output) {
        this.output = output;
    }

    public JenkinsLikeXmlHelper(XMLOutput output) {
        this.output = output;
    }

    public Object methodMissing(String name, Object args) {
        doInvokeMethod(new QName("",name), args);
        return null;
    }

    @SuppressWarnings({"ChainOfInstanceofChecks"})
    protected void doInvokeMethod(QName name, Object args) {
        List list = InvokerHelper.asList(args);

        Map attributes = Collections.EMPTY_MAP;
        Closure closure = null;
        String innerText = null;

        // figure out what parameters are what
        switch (list.size()) {
            case 0:
                break;
            case 1: {
                Object object = list.get(0);
                if (object instanceof Map) {
                    attributes = (Map) object;
                } else if (object instanceof Closure) {
                    closure = (Closure) object;
                    break;
                } else {
                    if (object!=null)
                        innerText = object.toString();
                }
                break;
            }
            case 2: {
                Object object1 = list.get(0);
                Object object2 = list.get(1);
                if (object1 instanceof Map) {
                    attributes = (Map) object1;
                    if (object2 instanceof Closure) {
                        closure = (Closure) object2;
                    } else
                    if(object2!=null) {
                        innerText = object2.toString();
                    }
                } else {
                    innerText = object1.toString();
                    if (object2 instanceof Closure) {
                        closure = (Closure) object2;
                    } else if (object2 instanceof Map) {
                        attributes = (Map) object2;
                    } else {
                        throw new MissingMethodException(name.toString(), getClass(), list.toArray());
                    }
                }
                break;
            }
            case 3: {
                Object arg0 = list.get(0);
                Object arg1 = list.get(1);
                Object arg2 = list.get(2);
                if (arg0 instanceof Map && arg2 instanceof Closure) {
                    closure = (Closure) arg2;
                    attributes = (Map) arg0;
                    innerText = arg1.toString();
                } else if (arg1 instanceof Map && arg2 instanceof Closure) {
                    closure = (Closure) arg2;
                    attributes = (Map) arg1;
                    innerText = arg0.toString();
                } else {
                    throw new MissingMethodException(name.toString(), getClass(), list.toArray());
                }
                break;
            }
            default:
                throw new MissingMethodException(name.toString(), getClass(), list.toArray());
        }

        this.attributes.clear();
        for (Map.Entry e : ((Map<?,?>)attributes).entrySet()) {
            Object v = e.getValue();
            if(v==null) continue;
            String attName = e.getKey().toString();
            this.attributes.addAttribute("",attName,attName,"CDATA", v.toString());
        }
        try {
            output.startElement(name.getNamespaceURI(),name.getLocalPart(),name.getQualifiedName(),this.attributes);
            if(closure!=null) {
                closure.setDelegate(this);
                closure.call();
            }
            if(innerText!=null)
                text(innerText);
            output.endElement(name.getNamespaceURI(),name.getLocalPart(),name.getQualifiedName());
        } catch (SAXException e) {
            throw new RuntimeException(e);  // what's the proper way to handle exceptions in Groovy?
        }
    }

    public void text(Object o) throws SAXException {
        if (o!=null)
            output.write(escape(o.toString()));
    }

    /**
     * Generates HTML fragment from string.
     *
     * <p>
     * The string representation of the object is assumed to produce proper HTML.
     * No further escaping is performed.
     *
     * @see #text(Object)
     */
    public void raw(Object o) throws SAXException {
        if (o!=null)
            output.write(o.toString());
    }

    private String escape(String v) {
        StringBuffer buf = new StringBuffer(v.length()+64);
        for( int i=0; i<v.length(); i++ ) {
            char ch = v.charAt(i);
            if(ch=='<')
                buf.append("&lt;");
            else
            if(ch=='>')
                buf.append("&gt;");
            else
            if(ch=='&')
                buf.append("&amp;");
            else
                buf.append(ch);
        }
        return buf.toString();
    }
}
