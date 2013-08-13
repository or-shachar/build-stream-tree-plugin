package hudson.plugins.clonejobs.job;

import hudson.model.Job;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/05/13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public interface JobsCloner {

    /**
     *
     * @param jobTargets a mapping of original project names -> desired cloned project names.
     * @return a mapping of originals-> cloned proejcts that resulted from this operation.
     *
     * this function clones all the jobs and then relinks them.
     * it handles misc jenkins stuff to keep it stable.
     */
    Map<Job, Job> clone(Map<String, String> jobTargets);
}
