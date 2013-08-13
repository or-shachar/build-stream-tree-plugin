package com.hp.commons.core.criteria;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/31/12
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 *
 * criteria wrapping a boolean value, for compatability with other interfaces,
 * such as filtering according to a map boolean value.
 * (eg CollectionUtils.filter(collection, new MapValueCriteria(new IsTrueCriteria))
 *
 * TODO extend from "GivenValueCriteria" where the constructor argument given value is true.
 * GivenValueCriteria will checks if the element .equals() the given value...
 *
 * this class is a singleton
 */
public class IsTrueCriteria implements Criteria<Boolean> {

    /**
     * singleton implementation
     */
    private IsTrueCriteria() {}

    /**
     * singleton implementation
     */
    private static IsTrueCriteria instance;

    /**
     * singleton implementation
     */
    public static IsTrueCriteria getInstance() {

        if (instance == null) {
            instance = new IsTrueCriteria();
        }

        return instance;
    }

    /**
     *
     * @param tested the element under test who we'd like to know if passes the criteria
     * @return the value of tested which is a boolean for this criteria to be applicable.
     */
    @Override
    public boolean isSuccessful(Boolean tested) {

        return (tested != null) && tested;
    }
}
