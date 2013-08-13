package hudson.plugins.clonejobs.view;

import hudson.model.Job;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/5/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 *
 * views generator for the cloned jobs. create views according to impl logic following a cloning process.
 * TODO should be made extensible?
 */
public interface ClonedProjectsViewGenerator {
    void cloneViews(Map<Job, Job> original2Cloned);
}
