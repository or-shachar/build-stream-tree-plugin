package com.hp.mercury.ci.jenkins.plugins.oo.core;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:41 PM
* To change this template use File | Settings | File Templates.
*/
public class OOFlow {

    //private List<String> argNames;
    private String id;

    /*
    public OOFlow(String id, List<String> argNames) {
        this.id = id;
        //this.argNames = argNames;
    }

    public List<String> getArgNames() {
        return argNames;
    }
    */

    public OOFlow(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
