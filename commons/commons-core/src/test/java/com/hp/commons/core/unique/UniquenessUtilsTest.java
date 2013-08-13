package com.hp.commons.core.unique;

import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class UniquenessUtilsTest {

    @Test(timeout = 5000)
    public void testRandomAlphanumeric() {

        //length of each random token
        final int size = 12;

        //number of strings to generate
        final int randomElementsSize = 100;

        //the minimal levenshtein distance we require between 2 consecutive strings.
        final int minLevenshteinDistance = 3;

        //this **set** will be used to count the **different** generated strings
        Set<String> set = new HashSet<String>();

        //create an initial value, to measure levenshtein against
        String previous = UniquenessUtils.randomAlphanumeric(size);
        set.add(previous);

        //the randomElementSize **- 1** is because we just created an element
        for (int i = 0 ; i < randomElementsSize - 1 ; i++) {

            final String current = UniquenessUtils.randomAlphanumeric(size);
            set.add(current);

            final int levenshteinDistance =
                    StringUtils.getLevenshteinDistance(previous, current, minLevenshteinDistance);

            Assert.assertTrue(
                    "levenshtein distance of 2 sequential random strings is under " +
                        minLevenshteinDistance,
                    levenshteinDistance == -1);

            previous = current;
        }

        Assert.assertEquals("failed to generate " + randomElementsSize + " *different* random strings",
                randomElementsSize,
                set.size());

    }

}
