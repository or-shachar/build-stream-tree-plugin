package com.hp.commons.core.string;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 12/2/12
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 *
 * encompasses common string related tasks
 *
 */
public class StringUtils {

    //utility class
    private StringUtils() { }

    /**
     *
     * removes whitespaces
     *
     * @param input
     * @return the input string, without any whitespaces.
     */
    public static String removeWhitespaces(String input) {
        return input.replaceAll("\\s","");
    }

    /**
     *
     * @param url a url that may or may not end with a "/"
     * @return the same url ending with "/"
     */
    public static String slashify(String url) {
        return url + (url.endsWith("/") ? "" : "/");
    }

    /**
     *
     * @param url a url that may or may not start with a "/"
     * @return the same url without "/" as prefix
     */
    public static String unslashifyPrefix(String url) {
        return url.startsWith("/") ? url.substring(1) : url;
    }

    /**
     *
     * @param e exception to transform into a string
     * @return exception class name, message and stacktrage in a string.
     */
    public static String exceptionToString(Exception e) {
        final ByteArrayOutputStream stacktraceBuffer = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(stacktraceBuffer));
        return e.toString() + new String(stacktraceBuffer.toByteArray());
    }

    public static void findStringsInString(Collection<String> searchedFor, Collection<String> found, String value) {
        for (String searchTerm : searchedFor) {
            if (value.contains(searchTerm)) {
                found.add(searchTerm);
            }
        }
    }
}
