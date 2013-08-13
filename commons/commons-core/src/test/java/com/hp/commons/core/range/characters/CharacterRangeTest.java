package com.hp.commons.core.range.characters;

import com.hp.commons.core.exception.NotImplementedException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/27/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharacterRangeTest {

    @Test(timeout = 5000)
    public void testNumeric() throws Exception {

        testSize(CharacterRange.Numeric, 10);
    }

    @Test(timeout = 5000)
    public void testAlphaNumeric() throws Exception {

        testSize(CharacterRange.LowercaseAlphabetic, 26);
    }

    @Test(timeout = 5000)
    public void testAlphaNumericEquality() throws Exception {

        final Iterator<Character> low = CharacterRange.LowercaseAlphabetic.iterator();
        final Iterator<Character> up = CharacterRange.UppercaseAlphabetic.iterator();

        while (low.hasNext()) {
            Assert.assertEquals(low.hasNext(), up.hasNext());
            Assert.assertEquals(
                    up.next().toString(),
                    low.next().toString().toUpperCase());
        }

        Assert.assertEquals(low.hasNext(), up.hasNext());
    }

    private void testSize(CharacterRange range, int requiredSize) {

        int size = range.getLength();

        Assert.assertEquals("range is of unexpected size", requiredSize, size);
        final Iterator<Character> iter = range.iterator();

        while (size-- > 0) {
            iter.next();
        }

        Assert.assertFalse("range should have finished at " + size, iter.hasNext());
    }

}
