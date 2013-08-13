package hudson.plugins.clonejobs.job.domlogic;

import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.string.StringUtils;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.plugins.clonejobs.CloneJobsAction;
import hudson.plugins.clonejobs.job.JobsCloner;
import jenkins.model.Jenkins;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/05/13
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 *
 * this jobs cloner doesn't rely on API, instead it reads the xml of each job, duplicates it, and modifies strings
 * matching old job names to strings matching new job names.
 */
public class XmlBasedJobsCloner implements JobsCloner {

    private final Handler<?, Job> postJobCloneProcessor;

    public XmlBasedJobsCloner(Handler<?, Job> postJobCloneProcessor) {
        DomUtils.saxReader = new SAXReader();
        this.postJobCloneProcessor = postJobCloneProcessor;
    }

    @Override
    public Map<Job, Job> clone(Map<String, String> jobTargets) {

        //colletion that is return value of function
        Map<Job, Job> ret = new HashMap<Job, Job>();

        for (Map.Entry<String,String> originalToTarget : jobTargets.entrySet()) {

            String jobName = originalToTarget.getKey();

            Job job = Jenkins.getInstance().getItemByFullName(jobName, Job.class);

            Document doc = DomUtils.jobToDom(job);
            if (doc == null) continue;

            CloneJobsAction.LOGGER.info("before relinking..." + DomUtils.docToString(doc));

            DomUtils.findAndReplaceInNodeValues(doc.getRootElement(), jobTargets);

            CloneJobsAction.LOGGER.info("after relinking..." + DomUtils.docToString(doc));

            String targetName = originalToTarget.getValue();

            final Job clonedJob = cloneJob(doc, targetName);
            if (clonedJob != null) {
                this.postJobCloneProcessor.apply(clonedJob);
                ret.put(job, clonedJob);
            }
        }

        return ret;
    }

    private Job cloneJob(Document doc, String targetName) {
        try {
            return (Job)Jenkins.getInstance().createProjectFromXML(targetName,
                    new ByteArrayInputStream(doc.asXML().getBytes()));
        }
        catch (IOException e) {

            CloneJobsAction.LOGGER.warning("failed to create project " +
                    targetName + "." + StringUtils.exceptionToString(e));

            return null;
        }
    }


}
