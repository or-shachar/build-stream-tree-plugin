package com.hp.commons.core.range.characters;

import com.hp.commons.core.exception.NotImplementedException;

import java.util.Iterator;

/**
 *
 * enum with several common character ranges.
 * useful for input validation, uid generation, text transformations etc...
 *
 * TODO create interface Range, and make BaseCharacterRange implement it and have the implement the interface and delegate to abstract...
 */
public enum CharacterRange implements Iterable<Character> {

    /**
     * represents the 0-9 numeric range
      */
    Numeric('0','9'),

    /**
     * represents the lower-case alphabetical range a-z
     */
    LowercaseAlphabetic('a','z'),

    /**
     * represents the upper-case alphabetical range A-Z
     */
    UppercaseAlphabetic('A','Z');

    /**
     * first character in range
     */
    private char firstLetter;

    /**
     * last character in range
     */
    private char lastLetter;

    /**
     * TODO rename: first, last
     * @param firstLetter first character in range
     * @param lastLetter last character in range
     */
    CharacterRange(char firstLetter, char lastLetter) {
        this.firstLetter = firstLetter;
        this.lastLetter = lastLetter;
    }

    /**
     *
     * @return {@link #firstLetter}
     * //TODO rename getRangeBeginning
     */
    public char getFirstLetter() {
        return firstLetter;
    }

    /**
     *
     * @return {@link #lastLetter}
     * //TODO rename getRangeEnd
     */
    public char getLastLetter() {
        return lastLetter;
    }

    /**
     *
     * @return the size of this range
     */
    //TODO rename: getCardinality?
    public int getLength() {

        //the +1 is because the range is inclusive:
        // to see this: if firstLetter and lastLetter are the same, the size should be 1 and not 0.
        return Math.abs(lastLetter - firstLetter) + 1;
    }

    /**
     * TODO only discrete ranges can be iterated in this manner, non discrete can't support traversal because they're infinite.
     *
     * @return an iterator that iterates this range's elements
     */
    @Override
    public Iterator<Character> iterator() {

        return new Iterator<Character>() {

            /**
             * state of iteration - next letter that will be returned
             */
            private Character currentLetter = CharacterRange.this.getFirstLetter();

            /**
             *
             * @return true iff the iteration has not reached the last letter
             */
            @Override
            public boolean hasNext() {

                return (currentLetter <= CharacterRange.this.getLastLetter());
            }

            /**
             *
             * @return the next character in the range
             */
            @Override
            public Character next() {

                //could be done in a single line, but clearer this way.
                char ret = currentLetter;
                currentLetter++;
                return ret;
            }

            /**
             * this method is not implemented.
             * @throws NotImplementedException
             */
            @Override
            public void remove() {
                throw new NotImplementedException();
            }
        };
    }
}