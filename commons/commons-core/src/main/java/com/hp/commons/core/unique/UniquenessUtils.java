package com.hp.commons.core.unique;

import com.hp.commons.core.range.characters.CharacterRange;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 10/14/12
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 *
 * Utility class for things relating to uniqueness
 *
 * TODO all *Utils classes should be Singleton instead of static, so that if fields will be required in them they will be loaded lazily
 */
public class UniquenessUtils {

    /**
     * cached character container for alpha-numeric chars.
     * TODO initialize by an iterator that iterates multiple character ranges, and a CollectionUtil method to convert iterator -> array, then make this a constant and remove the getter for this constant...
     * or maybe use a CombinedRange here which combines several other Ranges...?
     */
    private static char[] ALPHA_NUMERIC_CHARS = null;

    /**
     * getter that initializes {@link #ALPHA_NUMERIC_CHARS} if it's not initialized
     *
     * @return an initialized {@link #ALPHA_NUMERIC_CHARS} which contains a-z,A-Z,0-9
     */
    private static char[] getAlphaNumericChars() {

        //initialize the cached field if it's not set - for lazy loading...
        if (ALPHA_NUMERIC_CHARS == null) {

            //determine the size of the combined alphaNumeric range
            int alphaNumericCharactersCount = 0;

            //TODO don't use CharacterRange.values(), use the required ranges explicitly
            for (CharacterRange cr : CharacterRange.values()) {

                alphaNumericCharactersCount += cr.getLength();
            }

            ALPHA_NUMERIC_CHARS = new char[alphaNumericCharactersCount];

            //fill in the values from the nested ranges
            int index = 0;

            for (CharacterRange cr : CharacterRange.values()) {

                for (Character c : cr) {

                    ALPHA_NUMERIC_CHARS[index++] = c;
                }
            }
        }

        return ALPHA_NUMERIC_CHARS;
    }


    private static Random random = null;

    private static Random getRandom() {

        if (random == null) {
            random = new Random();
        }

        return random;
    }

    /**
     *
     * @param size of random alphanumeric string to generate
     * @return a random alpha numeric string of size "size" (param)
     */
    public static String randomAlphanumeric(int size) {

        char[] buf = new char[size];
        final Random random = getRandom();
        final char[] alphaNumericChars = getAlphaNumericChars();

        for (int i = 0 ; i < size ; i++) {
            buf[i] = randomAlphanumericChar(alphaNumericChars, random);
        }

        return new String(buf);
    }

    /**
     *
     * @return random alpha numeric character
     */
    public static char randomAlphanumericChar() {

        return randomAlphanumericChar(getAlphaNumericChars(), getRandom());
    }

    //TODO make public and move to CollectionUtils: getRandomElement(collection, random)
    //TODO need a random singleton in unique package, instead of carrying this random everywhere...
    private static char randomAlphanumericChar(char[] alphaNumericChars, Random random) {

        return alphaNumericChars[random.nextInt(alphaNumericChars.length)];
    }
}
