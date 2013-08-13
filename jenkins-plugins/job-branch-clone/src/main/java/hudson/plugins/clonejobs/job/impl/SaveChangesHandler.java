package hudson.plugins.clonejobs.job.impl;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.string.StringUtils;
import hudson.model.Job;
import hudson.plugins.clonejobs.CloneJobsAction;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/05/13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public class SaveChangesHandler implements Handler<Void, Job> {
    @Override
    public Void apply(Job node) {
        try {
            node.save();
        } catch (IOException e) {
            CloneJobsAction.LOGGER.warning(
                    "failed to save changes to " + node.getFullDisplayName() + ". " +
                    StringUtils.exceptionToString(e));
        }
        return null;
    }
}
