package com.hp.mercury.ci.jenkins.plugins.oo.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/5/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO change into an interface with extension point.
 *
 * make each static instance of MatchStrategy implement the interface, and @Extension
 *
 * the plugin allows you to change the state of the build, according to a condition on
 * a returned value from the OO response.
 * you must compare the response to some value.
 * this enum represents a collection of comparison operators.
 *
 */
public enum MatchStrategy {

    /**
     * true if OO value is equal to some predefined value
     */
    IS {
        /**
         *
         * @param s the value from the oo response
         * @param valueToCompareWith hardcoded value to compare with
         * @return true if inputs are the same, case agnostic
         */
        @Override
        public boolean matches(String s, String valueToCompareWith) {
            return s.equalsIgnoreCase(valueToCompareWith);
        }
    },

    /**
     * true if OO value contains some predefined string
     */
    CONTAINS {
        /**
         *
         * @param s value from OO
         * @param valueToCompareWith hardcoded value to compare with
         * @return
         */
        @Override
        public boolean matches(String s, String valueToCompareWith) {
            return s.toLowerCase().contains(valueToCompareWith.toLowerCase());
        }
    },

    /**
     * true if OO value starts with some predefined string
     */
    STARTS_WITH {
        /**
         *
         * @param s value from OO
         * @param valueToCompareWith hardcoded value to compare with
         * @return true iff s starts with valueToCompareWith (case agnostic)
         */
        @Override
        public boolean matches(String s, String valueToCompareWith) {
            return s.toLowerCase().startsWith(valueToCompareWith.toLowerCase());
        }
    },

    /**
     * true if value from OO is not the same as predefined value
     */
    DIFFERS_FROM {
        /**
         *
         * @param s value from OO
         * @param valueToCompareWith hardcoded value to compare with
         * @return true iff s is not equal to valueToCompareWith (case agnostic)
         */
        @Override
        public boolean matches(String s, String valueToCompareWith) {
            return !IS.matches(s, valueToCompareWith);
        }
    },

    /**
     * true if value from oo matches a regex
     */
    IS_A_REGEX_MATCH_FOR {
        /**
         *
         * @param s value from OO
         * @param valueToCompareWith hardcoded value to compare with
         * @return true if the string s matches the regex valueToCompareWith
         */
        @Override
        public boolean matches(String s, String valueToCompareWith) {
            return Pattern.matches(valueToCompareWith, s);
        }
    };

    /**
     *
     * @return list of all matchers
     */
    public static List<MatchStrategy> getMatchStrategies() {
        return Arrays.asList(MatchStrategy.values());
    }

    /**
     *
     * @param s value from OO
     * @param valueToCompareWith hardcoded value to compare with
     * @return depends on implementation, abstractly should be true iff s matches valueToCompareWith.
     */
    public abstract boolean matches(String s, String valueToCompareWith);
}
