package hudson.plugins.clonejobs.view.impl;

import hudson.model.Job;
import hudson.model.ListView;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.clonejobs.view.ClonedProjectsViewGenerator;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 2/5/12
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 *
 * view generator that puts all of the cloned jobs in one list view at the top level.
 */
public class TargetViewGenerator implements ClonedProjectsViewGenerator {

    /**
     * name of the view where all the cloned jobs should be placed
     * may or may not previously exist, so long as it's a ListView.
     */
    private String targetViewName;

    /**
     *
     * @param targetViewName name of the view where all the cloned jobs should be placed
     *                       may or may not previously exist, so long as it's a ListView.
     */
    public TargetViewGenerator(String targetViewName) {

        this.targetViewName = targetViewName;
    }

    @Override
    public void cloneViews(Map<Job, Job> original2Cloned) {

        //get or create the view
        ListView targetView = getView(targetViewName);
        if (targetView == null) {

            try {
                targetView = createView(targetViewName);
            }

            catch (IOException e) {
                //TODO: better to log than to die...
                throw new RuntimeException("failed to create view '" + targetViewName + "'", e);
            }
        }

        //put all the clones in the view
        Collection<Job> clones = original2Cloned.values();
        for (Job clone : clones) {

            try {
                targetView.add((TopLevelItem)clone);
            }

            catch (IOException e) {

                //TODO: better to log than to die...
                throw new RuntimeException(
                        "failed to add cloned job '" + clone.getFullDisplayName() +
                        "' to view '" + targetView.getViewName() + "'",e);
            }
        }
    }

    /**
     *
     * @param targetViewName the name of the top-level ListView view being sought.
     * @return the view if it exists as ListView or null if it doesn't exist.
     * other eventualties throw exceptions, but this should change.
     */
    private ListView getView(String targetViewName) {

        ListView targetView = null;

        final Collection<View> allViews = Jenkins.getInstance().getViews();
        for (View potentialTargetView : allViews) {

            if (potentialTargetView.getViewName().equalsIgnoreCase(targetViewName)) {

                if (potentialTargetView instanceof ListView) {
                    targetView = (ListView)potentialTargetView;
                    break;
                }

                else {
                    //TODO: better to log than to die
                    throw new RuntimeException("The view you specified as target is not a 'ListView' - " +
                            "it cannot contain projects directly.");
                }

            }
        }

        return targetView;
    }

    /**
     *
     * @param targetViewName the name of the top-level view you wish to create
     * @return the created ListView
     * @throws IOException
     */
    private ListView createView(String targetViewName) throws IOException {

        Jenkins.checkGoodName(targetViewName);

        ListView targetView = new ListView(targetViewName);
        Jenkins.getInstance().addView(targetView);

        return targetView;
    }
}
