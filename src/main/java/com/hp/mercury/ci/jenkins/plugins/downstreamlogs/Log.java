package com.hp.mercury.ci.jenkins.plugins.downstreamlogs;

import java.util.logging.Logger;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.WARNING;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 18/06/13
 * Time: 02:42
 * To change this template use File | Settings | File Templates.
 */
public class Log {


    private static Logger LOGGER = Logger.getLogger(DownstreamLogsSideMenuLink.class.getName());

    public static DownstreamLogsDebugLoggerLogic debugLogger;
    static {
        updateLogger();
    }

    private static DownstreamLogsDebugLoggerLogic getDebugLogger() {
        return debugLogger;
    }

    /**
     * this is done at finest level
     * @param msg
     */

    public static void debug(String msg) {
        getDebugLogger().debug(msg);
    }

    /* deprecated
    public static void incTab() {
        getDebugLogger().incTab();
    }

    public static void deTab() {
        getDebugLogger().deTab();
    }
    */

    /**
     *
     * this is done at warning level
     */

    public static void warning(String s) {
        getDebugLogger().warning(s);
    }

    /**
     * this is done at finer level
     * @param clazz
     * @param method
     * @param throwable
     */

    public static void throwing(String clazz, String method, Throwable throwable) {
        getDebugLogger().throwing(clazz, method, throwable);
    }

    public static void updateLogger() {
        try {
        debugLogger = DownstreamLogsAction.getDescriptorStatically().getDebugMode() ?
                new DebugLoggerImpl() :
                NOPDebugLogger.getInstance();
        }
        catch (Exception e) {
            debugLogger = NOPDebugLogger.getInstance();
        }
    }

    public interface DownstreamLogsDebugLoggerLogic {
        void warning(String msg);

        void throwing(String clazz, String method, Throwable throwable);

        void debug(String msg);

        void incTab();

        void deTab();
    }

    private static class DebugLoggerImpl implements DownstreamLogsDebugLoggerLogic {

        public String msgPrefix = " ";

        /**
         *
         * this is done at warning level
         * @param msg
         */
        public void warning(String msg) {
            LOGGER.log(WARNING, msgPrefix + msg);
        }

        /**
         * this is done at finer level
         * @param clazz
         * @param method
         * @param throwable
         */
        public void throwing(String clazz, String method, Throwable throwable) {
            LOGGER.throwing(clazz, method, throwable);
        }

        /**
         * this is done at finest level
         * @param msg
         */
        public void debug(String msg) {
            LOGGER.log(FINEST, msgPrefix + msg);
        }

        public void incTab() {
            msgPrefix += "  ";
        }

        public void deTab() {
            msgPrefix = msgPrefix.substring(2);
        }
    }

    private static class NOPDebugLogger implements DownstreamLogsDebugLoggerLogic {
        public void warning(String msg) {
        }

        public void throwing(String clazz, String method, Throwable throwable) {
        }

        public void debug(String msg) {
        }

        public void incTab() {
        }

        public void deTab() {
        }

        private NOPDebugLogger() {}
        private static DownstreamLogsDebugLoggerLogic instance = null;
        public static DownstreamLogsDebugLoggerLogic getInstance() {
            if (instance == null) {
                instance = new NOPDebugLogger();
            }
            return instance;
        }
    }
}
