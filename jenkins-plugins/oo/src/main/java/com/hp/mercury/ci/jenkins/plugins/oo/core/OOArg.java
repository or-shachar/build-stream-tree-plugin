package com.hp.mercury.ci.jenkins.plugins.oo.core;

import org.kohsuke.stapler.DataBoundConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:41 PM
* To change this template use File | Settings | File Templates.
*/
public class OOArg {

    private String value;

    private String name;

    //for jaxb
    public OOArg() {}

    @DataBoundConstructor
    public OOArg(String name, String value) {

        this.name = name;
        this.value = value;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OOArg{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
