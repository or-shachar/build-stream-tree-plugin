package hudson.plugins.clonejobs.job.domlogic;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.handler.Handler;
import hudson.model.Job;
import jenkins.model.Jenkins;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 28/05/13
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
public class XmlBasedJobPropagator implements Propagator<Job> {

    @Override
    public Collection<Job> propagate(Job source) {

        Collection<String> found = new HashSet<String>();

        final Document jobDoc = DomUtils.jobToDom(source);

        DomUtils.findOccurrences(
                jobDoc.getRootElement(),
                Jenkins.getInstance().getJobNames(),
                found);

        return CollectionUtils.map(
                found,
                //translate jobName to job instance to fit API
                new Handler<Job, String>() {
                    @Override
                    public Job apply(String jobName) {
                        return Jenkins.getInstance().getItemByFullName(jobName, Job.class);
                    }
                }
        );
    }
}
