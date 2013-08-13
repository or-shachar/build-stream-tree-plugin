package hudson.plugins.clonejobs;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.TransientViewActionFactory;
import hudson.model.View;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/22/12
 * Time: 10:25 PM
 *
 * this class adds the jenkins ui link on the left sided menu when you look at a view.
 * it is not the link itself, it just adds it.
 * for the link itself see {@link CloneJobsAction}.
 *
 */

public class SidePanelAddition extends TransientViewActionFactory {

    @Extension
    public static TransientViewActionFactory newInstance()  {
        return new SidePanelAddition();
    }

    @Override
    public List<Action> createFor(View view) {
        return Arrays.asList((Action)new CloneJobsAction(view));
    }
}
