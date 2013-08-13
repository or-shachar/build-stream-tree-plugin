package com.hp.mercury.ci.jenkins.plugins.conditionaltrigger.conditions;

import hudson.model.Result;
import hudson.model.Run;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/3/12
 * Time: 9:20 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ResultComparingLogic {

    AsGoodAs {
        @Override
        public boolean resultAcceptable(Run lastBuild, Result result) {
            return lastBuild.getResult().isBetterOrEqualTo(result);
        }

        @Override
        public String getDisplayName() {
            return "As Good As";
        }
    },

    AsBadAs {
        @Override
        public boolean resultAcceptable(Run lastBuild, Result result) {
            return lastBuild.getResult().isWorseOrEqualTo(result);
        }

        @Override
        public String getDisplayName() {
            return "As Bad As";
        }

    };

    public abstract boolean resultAcceptable(Run lastBuild, Result result);
    public abstract String getDisplayName();
}
