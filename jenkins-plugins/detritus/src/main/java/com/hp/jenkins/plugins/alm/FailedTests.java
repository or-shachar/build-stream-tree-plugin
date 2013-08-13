package com.hp.jenkins.plugins.alm;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.test.AbstractTestResultAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class FailedTests {

    private Map<DetritusBuildWrapper, AbstractTestResultAction> map;

    public FailedTests() {

        map = new HashMap<DetritusBuildWrapper, AbstractTestResultAction>();
    }

    public void put(DetritusBuildWrapper testBuild, AbstractTestResultAction testResults) {

        map.put(testBuild, testResults);
    }

    public Collection<FailedTestBuildConfiguration> get() {

        return CollectionUtils.map(
                map.entrySet(),
                new Handler<FailedTestBuildConfiguration, Map.Entry<DetritusBuildWrapper, AbstractTestResultAction>>() {

            @Override
            public FailedTestBuildConfiguration apply(Map.Entry<DetritusBuildWrapper, AbstractTestResultAction> input) {
                return new FailedTestBuildConfiguration(input.getKey(), input.getValue());
            }
        });
    }


    @Override
    public String toString() {

        StringBuffer ret = new StringBuffer("FailedTests{");

        for (Map.Entry<DetritusBuildWrapper, AbstractTestResultAction> entry : map.entrySet()) {

            ret.append(entry.getKey().toString()).append("=");

            for (CaseResult res : (List<CaseResult>)entry.getValue().getFailedTests()) {

                ret.append(res.getClassName()).append("(");

                for (hudson.tasks.test.TestResult tres : (Collection<hudson.tasks.test.TestResult>)res.getFailedTests()) {
                    ret.append(tres.toPrettyString()).append(",");
                }

                ret.append(")");
            }

        }

        ret.append("}");

        return ret.toString();
    }
}
