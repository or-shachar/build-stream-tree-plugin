package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import hudson.model.Job;
import hudson.model.ListView;
import hudson.model.View;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/4/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 *
 * this class returns true if the view contains any of the jobs in the collection given on initialization.
 */
public class ProjectContainmentDetectorHandler implements Handler<Boolean, View> {

    /**
     * jobs whose presence should be verified in the view
     */
    private Set<Job> jobs;

    /**
     *
     * @param jobs whose presence should be verified in the view
     */
    public ProjectContainmentDetectorHandler(Set<Job> jobs) {
        this.jobs =  jobs;
    }

    @Override
    public Boolean apply(View node) {

        if (node instanceof ListView) {

            ListView listView = (ListView)node;
            return CollectionUtils.containsAny(listView.getItems(), this.jobs);
        }

        return false;
    }
}
