package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 06/07/13
 * Time: 04:36
 * To change this template use File | Settings | File Templates.
 */
public class Log {

    public static Logger LOGGER = Logger.getLogger(Log.class.getName());

    public static void log(Level level, String msg) {
        LOGGER.log(level, msg);
    }
}
