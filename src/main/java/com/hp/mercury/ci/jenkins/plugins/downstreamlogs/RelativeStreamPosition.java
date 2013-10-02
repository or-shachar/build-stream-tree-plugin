package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 18/06/13
 * Time: 02:07
 * To change this template use File | Settings | File Templates.
 */
public enum RelativeStreamPosition {
    UPSTREAM("up"),
    SIBLING("right"),
    DOWNSTREAM("down"),
    ME("me");

    private String imageName = null;

    RelativeStreamPosition(String imageName) {
        this.imageName = imageName;
    }

    String getImageName() {
        return imageName;
    }
}
