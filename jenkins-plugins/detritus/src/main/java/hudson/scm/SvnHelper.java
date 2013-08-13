package hudson.scm;

import com.hp.jenkins.plugins.alm.DetritusListener;
import hudson.model.Run;

import java.util.Collections;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 8/8/12
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 * nathang: we need this class because SVNRevisionState is package protected.
 * that's why this class is in hudson.scm (the same as SVNRevisionState).
 *
 */
public class SvnHelper {

    public static Map<String,Long> extractRepoVersionMapping(Run run) {

        final SCMRevisionState state = run.getAction(SCMRevisionState.class);

        if (state instanceof SVNRevisionState) {

            return ((SVNRevisionState) state).revisions;
        }

        else {

            DetritusListener.getInstance().error("detritus only supports SVN repos.");
            return Collections.emptyMap();
        }
    }
}
