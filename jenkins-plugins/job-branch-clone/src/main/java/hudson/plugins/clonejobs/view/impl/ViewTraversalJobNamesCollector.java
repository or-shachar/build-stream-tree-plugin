package hudson.plugins.clonejobs.view.impl;

import com.hp.commons.core.handler.VoidHandler;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.model.View;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/24/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 *
 * traverses a view and collects the names for all of the recursively contained jobs
 *
 * TODO this class collects the actual jobs, not their names... name and doc should change appropriatley
 */
public class ViewTraversalJobNamesCollector implements VoidHandler<View> {

    /**
     * the collection to store the names of visited jobs
     */
    private Collection<Job> collectedNames;

    /**
     *
     * @param col the collection to store the names of visited jobs
     */
    public ViewTraversalJobNamesCollector(Collection<Job> col) {
        this.collectedNames = col;
    }

    @Override
    public Void apply(View source) {

        final Collection<TopLevelItem> items = source.getItems();

        for (TopLevelItem item : items) {

            if (item instanceof Job) {
                final Job job = (Job) item;
                collectedNames.add(job);
            }
        }

        return null;
    }
}
