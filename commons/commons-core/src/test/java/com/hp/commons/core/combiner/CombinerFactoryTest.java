package com.hp.commons.core.combiner;

import com.hp.commons.core.handler.Handler;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 11/26/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CombinerFactoryTest {

    @Test
    public void testCombine() throws Exception {

        @SuppressWarnings("unchecked")
        final Handler<Integer, Integer> combined = CombinerFactory.<Handler>combine(
                Handler.class,
                Arrays.asList(
                        new IncHandler(),
                        new IncHandler(),
                        new IncHandler()
                ),
                new CombinationBuilder() {

                    private int sum = 0;

                    @Override
                    public void instance(Object inst) {
                        sum += (Integer) inst;
                    }

                    @Override
                    public Object combine() {
                        return sum;
                    }
                });

        Assert.assertEquals(
                "3 incrementors of zero failed to result in 3",
                (Object)3,
                combined.apply(0));
    }

    //TODO possibly create commons-testing and put this there?
    public static class IncHandler implements Handler<Integer, Integer>{

        @Override
        public Integer apply(Integer node) {
            return node + 1;
        }
    }
}
