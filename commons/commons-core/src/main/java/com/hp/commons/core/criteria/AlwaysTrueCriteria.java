package com.hp.commons.core.criteria;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/15/12
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 *
 * a criteria implementation that always returns true.
 * the all-encompassing criteria.
 *
 * this class is a singleton.
 */
public class AlwaysTrueCriteria implements Criteria {


    /**
     * singleton implementation
     */
    private static AlwaysTrueCriteria instance;

    /**
     * singleton implementation
     */
    private AlwaysTrueCriteria() {};

    /**
     * singleton implementation
     */
    public static <T> Criteria<T> getInstance() {

        if (instance == null) {
            instance = new AlwaysTrueCriteria();
        }

        /**
         * does a trick by ignoring type safety as it is irrelevant, thus supporting all types.
         */
        return (Criteria<T>)instance;
    }

    /**
     *
     * @param tested irrelevant
     * @return true
     */
    @Override
    public boolean isSuccessful(Object tested) {
        return true;
    }
}
