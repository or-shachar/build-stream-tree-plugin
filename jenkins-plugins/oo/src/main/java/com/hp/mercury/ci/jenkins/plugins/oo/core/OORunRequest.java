package com.hp.mercury.ci.jenkins.plugins.oo.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:44 PM
* To change this template use File | Settings | File Templates.
*/ /* request format:
<?xml version=\"1.0\" ?>
<run>
    <request>
        <arg name=\"name0\">value0</arg>
        <arg name=\"name1\">value1</arg>
        <arg name=\"name2\">value2</arg>
    </request>
</run>
*/
@XmlRootElement(name = "run")
public class OORunRequest {

    private OOServer server;
    private OOFlow flow;
    private List<OOArg> args;

    //for jaxb
    public OORunRequest() {}

    public OORunRequest(OOServer selectedOOServer, OOFlow flow, List<OOArg> args) {
        this.server = selectedOOServer;
        this.flow = flow;
        this.args = args;
    }

    public OOServer getServer() {
        return server;
    }

    public OOFlow getFlow() {
        return flow;
    }

    @XmlElementWrapper(name = "request")
    @XmlElement(name = "arg")
    public List<OOArg> getArgs() {
        return args;
    }

    public void setArgs(List<OOArg> args) {
        this.args = args;
    }
}
