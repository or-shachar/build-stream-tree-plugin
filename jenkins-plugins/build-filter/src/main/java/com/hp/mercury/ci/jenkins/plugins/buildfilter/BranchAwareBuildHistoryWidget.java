package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.Functions;
import hudson.model.*;
import hudson.model.Queue;
import hudson.util.Iterators;
import hudson.widgets.HistoryWidget;
import hudson.widgets.Messages;
import hudson.widgets.Widget;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 24/06/13
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */

public class BranchAwareBuildHistoryWidget extends Widget {
    protected static final HistoryWidget.Adapter<Run> HISTORY_ADAPTER = new HistoryWidget.Adapter<Run>() {
        public int compare(Run record, String key) {
            try {
                int k = Integer.parseInt(key);
                return record.getNumber() - k;
            } catch (NumberFormatException nfe) {
                return String.valueOf(record.getNumber()).compareTo(key);
            }
        }

        public String getKey(Run record) {
            return String.valueOf(record.getNumber());
        }

        public boolean isBuilding(Run record) {
            return record.isBuilding();
        }

        public String getNextKey(String key) {
            try {
                int k = Integer.parseInt(key);
                return String.valueOf(k + 1);
            } catch (NumberFormatException nfe) {
                return "-unable to determine next key-";
            }
        }
    };

    /**
     * @param owner
     *      The parent model object that owns this widget.

    public BranchAwareBuildHistoryWidget(Queue.Task owner, Iterable<T> baseList,Adapter<? super T> adapter) {
        super(owner,baseList, adapter);
    }
     */

    /**
     * Returns the first queue item if the owner is scheduled for execution in the queue.
     */
    public Queue.Item getQueuedItem() {
        return Jenkins.getInstance().getQueue().getItem(owner);
    }

    /**
     * Returns the queue item if the owner is scheduled for execution in the queue, in REVERSE ORDER
     */
    public List<Queue.Item> getQueuedItems() {
        LinkedList<Queue.Item> list = new LinkedList<Queue.Item>();
        for (Queue.Item item : Jenkins.getInstance().getQueue().getApproximateItemsQuickly()) {
            if (item.task == owner) {
                list.addFirst(item);
            }
        }
        return list;
    }

    /**
     * The given data model of records. Newer ones first.
     */
    public Iterable<Run> baseList;

    /**
     * Indicates the next build number that client ajax should fetch.
     */
    private String nextBuildNumberToFetch;

    /**
     * URL of the {@link #owner}.
     */
    public final String baseUrl;

    public final Queue.Task owner;

    private boolean trimmed;

    public final HistoryWidget.Adapter<Run> adapter;

    /**
     * First transient build record. Everything >= this will be discarded when AJAX call is made.
     */
    private String firstTransientBuildKey;

    /**
     * @param owner
     *      The parent model object that owns this widget.
     */
    public BranchAwareBuildHistoryWidget(Queue.Task owner, Iterable<Run> baseList, HistoryWidget.Adapter<Run> adapter) {
        this.adapter = adapter;
        this.baseList = baseList;
        this.baseUrl = Functions.getNearestAncestorUrl(Stapler.getCurrentRequest(), owner);
        this.owner = owner;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Title of the widget.
     */
    public String getDisplayName() {
        return Messages.BuildHistoryWidget_DisplayName();
    }

    @Override
    public String getUrlName() {
        return "buildHistory";
    }

    public String getFirstTransientBuildKey() {
        return firstTransientBuildKey;
    }

    private Iterable<Run> updateFirstTransientBuildKey(Iterable<Run> source) {
        String key=null;
        for (Run t : source)
            if(adapter.isBuilding(t))
                key = adapter.getKey(t);
        firstTransientBuildKey = key;
        return source;
    }

    /**
     * The records to be rendered this time.
     */
    public Iterable<Run> getRenderList() {

        String selectedBranch = Stapler.getCurrentRequest().getParameter("buildsFilter");
        if (selectedBranch == null || selectedBranch.isEmpty()) {
            selectedBranch = Stapler.getCurrentRequest().getHeader("buildsFilter");
        }

        if(trimmed) {
            List<Run> lst;
            if (baseList instanceof List) {

                lst = new ArrayList<Run>(((List) baseList).size());

                for (Run run : baseList) {
                    if (doesRunBelongToBranch(selectedBranch, run)) {
                        lst.add(run);
                    }
                }

                if(lst.size()>THRESHOLD) {
                    return updateFirstTransientBuildKey(lst.subList(0,THRESHOLD));
                }

                trimmed=false;
                return updateFirstTransientBuildKey(lst);
            } else {
                lst = new ArrayList<Run>(THRESHOLD);
                Iterator<Run> itr = baseList.iterator();
                while(lst.size()<=THRESHOLD && itr.hasNext()) {
                    final Run run = itr.next();

                    if (doesRunBelongToBranch(selectedBranch, run)) {
                        lst.add(run);
                    }
                }
                trimmed = itr.hasNext(); // if we don't have enough items in the base list, setting this to false will optimize the next getRenderList() invocation.
                return updateFirstTransientBuildKey(lst);
            }
        } else {

            final ArrayList<Run> branchFilteredLists = new ArrayList<Run>();
            for (Run run : baseList) {
                if (doesRunBelongToBranch(selectedBranch, run)) {
                    branchFilteredLists.add(run);
                }
            }
            // to prevent baseList's concrete type from getting picked up by <j:forEach> in view
            return updateFirstTransientBuildKey(Iterators.wrap(branchFilteredLists));
        }
    }

    public static Boolean doesRunBelongToBranch(String selectedBranch, Run run) {

        if (selectedBranch == null || selectedBranch.equals("")) {
            return true;
        }

        //note the importance of the order, the run branch might be null!
        return selectedBranch.equals(getRunBranch(run));
    }

    public static Logger LOGGER = Logger.getLogger(BranchAwareBuildHistoryWidget.class.getName());

    //d61b3eaf920c82fd5b1a19059e8c6b7f481e35e9
    private static Pattern gitHashPattern = Pattern.compile("[a-fA-F0-9]{40}");
    public static String getRunBranch(Run run) {

        final BuildHistoryWidgetOverridingJobProperty.DescriptorImpl descriptor =
                (BuildHistoryWidgetOverridingJobProperty.DescriptorImpl) Jenkins.getInstance().getDescriptor(
                        BuildHistoryWidgetOverridingJobProperty.class);

        final List<ParametersAction> actions = run.getActions(ParametersAction.class);

        String branch = null;
        for (ParametersAction action : actions) {

            for (String branchParameterName : descriptor.getBranchParameterNamesAsStrings()) {
                final ParameterValue pvalue = action.getParameter(branchParameterName);
                if (pvalue instanceof StringParameterValue) {
                    final String value = ((StringParameterValue) pvalue).value;
                    if (value != null && !value.trim().isEmpty()) {
                        branch = value.trim();
                        break;
                    }
                }
            }
        }

        if (branch != null && descriptor.getIgnoreHashes() && gitHashPattern.matcher(branch).matches()) {
            branch = null;
        }

        String repository = null;
        for (ParametersAction action : actions) {

            for (String repositoryParameterName : descriptor.getRepositoryParameterNamesAsStrings()) {
                final ParameterValue pvalue = action.getParameter(repositoryParameterName);
                if (pvalue instanceof StringParameterValue) {
                    String value = ((StringParameterValue) pvalue).value;
                    if (value != null && !value.trim().isEmpty()) {
                        value = value.trim();
                        repository = value.substring(value.lastIndexOf('/') + 1);
                        final int dotIndex = repository.lastIndexOf('.');
                        if (dotIndex >= 0) {
                            repository = repository.substring(0, dotIndex);
                        }

                        break;
                    }
                }
            }
        }

        String uid = (repository == null ? "" : repository + " / ") + (branch == null ? "" : branch);
        uid = uid.trim();

        if (uid.isEmpty()) {
            uid = null;
        }

        return uid;
    }

    public boolean isTrimmed() {
        return trimmed;
    }

    public void setTrimmed(boolean trimmed) {
        this.trimmed = trimmed;
    }

    /**
     * Handles AJAX requests from browsers to update build history.
     *
     * @param n
     *      The build 'number' to fetch. This is string because various variants
     *      uses non-numbers as the build key.
     */
    public void doAjax( StaplerRequest req, StaplerResponse rsp,
                        @Header("n") String n) throws IOException, ServletException {

        if (n==null)    throw HttpResponses.error(SC_BAD_REQUEST, new Exception("Missing the 'n' HTTP header"));

        rsp.setContentType("text/html;charset=UTF-8");

        // pick up builds to send back
        List<Run> items = new ArrayList<Run>();

        String nn=null; // we'll compute next n here

        // list up all builds >=n.
        for (Run t : baseList) {
            if(adapter.compare(t,n)>=0) {
                items.add(t);
                if(adapter.isBuilding(t))
                    nn = adapter.getKey(t); // the next fetch should start from youngest build in progress
            } else
                break;
        }

        if (nn==null) {
            if (items.isEmpty()) {
                // nothing to report back. next fetch should retry the same 'n'
                nn=n;
            } else {
                // every record fetched this time is frozen. next fetch should start from the next build
                nn=adapter.getNextKey(adapter.getKey(items.get(0)));
            }
        }

        baseList = items;

        rsp.setHeader("n",nn);
        firstTransientBuildKey = nn; // all builds >= nn should be marked transient

        req.getView(this,"ajaxBuildHistory.jelly").forward(req,rsp);

    }

    private static final int THRESHOLD = Integer.getInteger(HistoryWidget.class.getName()+".threshold",30);

    public String getNextBuildNumberToFetch() {
        return nextBuildNumberToFetch;
    }

    public void setNextBuildNumberToFetch(String nextBuildNumberToFetch) {
        this.nextBuildNumberToFetch = nextBuildNumberToFetch;
    }
}
