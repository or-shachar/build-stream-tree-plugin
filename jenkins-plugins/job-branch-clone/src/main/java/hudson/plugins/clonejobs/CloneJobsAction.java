package hudson.plugins.clonejobs;

import com.hp.commons.core.combiner.CombinerFactory;
import com.hp.commons.core.combiner.impl.NullCombinationBuilder;
import com.hp.commons.core.handler.Handler;
import com.hp.commons.core.graph.impl.SubgraphPropagatorBuilder;
import com.hp.commons.core.graph.impl.TraversalFactory;
import com.hp.commons.core.graph.propagator.MapBasedPropagator;
import com.hp.commons.core.graph.propagator.Propagator;
import com.hp.commons.core.graph.traverser.Traversal;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.View;
import hudson.plugins.clonejobs.job.domlogic.XmlBasedJobsCloner;
import hudson.plugins.clonejobs.job.impl.*;
import hudson.plugins.clonejobs.view.ClonedProjectsViewGenerator;
import hudson.plugins.clonejobs.view.impl.*;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 1/22/12
 * Time: 10:34 PM
 *
 * this class encompasses all functionality of jobs clone.
 * the "start" of the flow is the {@link #doIndex(StaplerRequest, StaplerResponse)} method.
 *
 * TODO: unify all plugin packages to the same conventions
 *
 */
//we suppress unused warnings because a lot of methods exist for jelly and are used by reflection
@SuppressWarnings("unused")
public class CloneJobsAction implements Action {

    public static Logger LOGGER = Logger.getLogger(CloneJobsAction.class.getName());

    private JsProxyValidationHandler validationHandler;

    /**
     *  each view has an instance of this class, which is responsible for cloning jobs inside this view.
     */
    private View view;

    /**
     *  collection of all jobs in this view including subviews.
     */
    private Collection<Job> hierarchicJobs;

    /**
     * collection of names of jobs that the user has selected to clone
     */
    private Set<String> selectedJobNames;

    /**
     * mapping of jobs to jobs propagated from them, by downstream or build-step triggering etc
     */
    private Map<Job, Collection<Job>> propagatedJobs;

    /**
     * collection of names of propagated jobs that the user has selected
     */
    private Set<String> selectedPropagatedJobNames;

    /**
     * used to traverse views and execute generic operations on them
     */
    private Traversal<Void, Void, View> viewsTraversal;

    /**
     * given a view returns all of its directly nested views.
     */
    private Propagator<View> nestedViewsPropagator;

    /**
     * collection of "global" views, the first tab headers that you see when browsing jenkins, that we wish to clone.
     */
    private Collection<View> tabViewsToClone;

    /**
     * propagator whose propagation results given a view are the view's neighbours according to the subgraph of the
     * views graph that is made of views containing jobs the user has selected (e.g. {@link #selectedPropagatedJobNames}
     * + {@link #selectedJobNames}) and their parents all the way to the roots of the forest e.g.
     * {@link #tabViewsToClone}
     */
    private Propagator<View> viewsSubgraphPropagtor;

    /**
     * set containing all jobs that should be cloned (e.g. composed of {@link #selectedPropagatedJobNames} +
     * {@link #selectedJobNames}),
     */
    private Set<Job> jobsToClone;

    /**
     * mapping used for replacing the labels used by the cloned jobs, where:<br/>
     * 1. keys are the current labels collected from all the jobs that we wish to clone<br/>
     * 2. user defined values indicating which label should be used instead in the newly cloned jobs.
     */
    private Map<String, String> labelMapping;

    /**
    * mapping used for replacing the SVN repository urls used by the cloned jobs, where:<br/>
    * 1. keys are the current urls collected from all the jobs that we wish to clone<br/>
    * 2. user defined values indicating which repository url should be used instead in the newly cloned jobs.
     *
     * very important to use LinkedHashMap specifically, because it guarantees iteration order.
     * when displaying scms to the user in the jelly UI the user response is sent as the indexes the user has chosen.
     * to make sure the server iterates the collection in the same order as the client and thus the indexes actually
     * mean the same thing, we must have this consistency.
     */
    private LinkedHashMap<String, String> scmMapping;

    /**
     * @param view the view which where the UI Clone Jobs link was clicked, indicating we wish to clone jobs
     *             that are contained in this view.
     */
    public CloneJobsAction(View view) {

        this.view = view;
        this.nestedViewsPropagator = new NestedViewPropagator();
        this.viewsTraversal = TraversalFactory.getTraversal(null, this.nestedViewsPropagator);
        this.validationHandler = new JsProxyValidationHandler();
    }

    /**
     *
     * @return the view where the UI link of Clone Jobs was clicked.
     * this is the view containing the jobs we want to clone.
     */
    public View getView() {
        return view;
    }

    /**
     *
     * @return the plugin symbol image used in the UI "Clone Jobs" link, if the user has permissions to view this link.
     * if the user does not (e.g. is not an administrator) then we return null, thus preventing the link from being
     * rendered.
     */
    @Override
    public String getIconFileName() {

        //make the link available only to administrators
        if (Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER)) {
            //this is the puzzle icon
            return "plugin.png";
        }
        else {
            return null;
        }
    }

    public JsProxyValidationHandler getJsProxy(){
        return validationHandler;
    }

    @Override
    /**
     * displayed label of the link to the cloning functionality
     */
    public String getDisplayName() {
        return "Clone Jobs";
    }

    @Override
    public String getUrlName() {
        return "clone";
    }

    /**
     *
     * @return the propagated jobs.
     * @see #propagatedJobs
     */
    public Map<Job, Collection<Job>> getPropagatedJobs() {
        return this.propagatedJobs;
    }

    /**
     * when accessing the /clone/ url from the link you are bound by stapler to this instance.
     * the default token is "index" and so stapler binds you to doIndex(). see overview for details.
     *
     * main method - immediately redirects to the selectJobs.jelly view
    * */
    public void doIndex(StaplerRequest request, StaplerResponse response) throws IOException {

        //start the flow, go to selectJobs.jelly webpage.
        response.sendRedirect("selectJobs");
    }

    /**
     * this function is called once the user has selected the jobs to clone.
     * @param request contains the form submission containing the user input.
     */
    public void doProcessSelectedJobs(StaplerRequest request, StaplerResponse response) throws ServletException, IOException {

        this.selectedJobNames = new LinkedHashSet<String>();

        final JSONObject submittedForm = request.getSubmittedForm();

        //retrieve mapping of job name (nestedly contained in this view) to boolean meaning "was selected by user".
        final Set<Map.Entry<String, Boolean>> selectionCheckboxes =
                (Set<Map.Entry<String, Boolean>>)submittedForm.entrySet();

        //put the jobs that were selected according to the map in the selectedJobNames member.
        saveSelectedJobs(selectionCheckboxes, this.selectedJobNames);

        //from the selected jobs, create the collection of dependant/propagated (e.g. downstream, copy artifact ...)
        determinePropagatedJobs();

        //move to next step in flow, the selectPropagatedJobs.jelly webpage.
        response.sendRedirect("selectPropagatedJobs");
    }

    /**
     * this method is reached once the user has selected which of the propagated jobs should be cloned.
     * @param request containing the user input propagated job name -> boolean indicating if selected.
     *
     */
    public void doSaveSelectedPropagatedJobs(StaplerRequest request, StaplerResponse response) throws ServletException, IOException {

        //if we somehow skipped the previous url... maybe a bookmark or an old session.
        if (this.selectedJobNames == null) {

            //redirect to the first screen in the flow e.g. go to doIndex() method.
            response.sendRedirect(Jenkins.getInstance().getRootUrl() + this.getUrlName());
            return;
        }

        this.selectedPropagatedJobNames = new HashSet<String>();

        //obtain map of propagated job name -> boolean indicating if it was selected by user
        final JSONObject submittedForm = request.getSubmittedForm();
        final Set<Map.Entry<String, Boolean>> selectionCheckboxes =
            (Set<Map.Entry<String, Boolean>>)submittedForm.entrySet();

        //update the collection of propagated jobs from the user input
        saveSelectedJobs(selectionCheckboxes, this.selectedPropagatedJobNames);

        this.jobsToClone = determineJobsToClone();

        //find the views subgraph containing any path to a selected job
        determineViewsSubgraph();

        //collect all used labels as in node groups where the cloned selected jobs should be executed.
        determineUsedLabels();

        //redirect to the next and final UI flow stage, rendered from configureCloneTargets.jelly
        response.sendRedirect("configureCloneTargets");
    }

    /**
     *
     * @return collection of jobs that were selected for cloning. used by jelly scripts.
     * @see #jobsToClone
     */
    public Set<Job> getJobsToClone() {

        return jobsToClone;
    }

    /**
     * collect all used labels ({@link hudson.model.Label} as in node groups) where the jobs that are to be cloned are
     * currently executed.
     *
     * TODO: since this also handles SCM changes, function name should change...
     */
    private void determineUsedLabels() {

        //initialize collections that will be used to store the Labels and SCMs
        final Set<String> collectedLabels = new HashSet<String>();
        final Set<String> collectedScms = new HashSet<String>();

        //create one handler that combines Label gathering and SCM gathering.
        Handler<?,Job> handler = CombinerFactory.combine(Handler.class,
                Arrays.asList(
                        new LabelGatheringHandler(collectedLabels),
                        new SvnScmGatheringHandler(collectedScms)),
                NullCombinationBuilder.getInstance());

        //collect the required data from jobs to clone
        for (Job job :this.jobsToClone) {
            handler.apply(job);
        }

        //TODO: collection utils mapping functions should have a function expanding a collection to an ID map like so.
        //create and ID mapping, initializing the to default "unchanged" values
        this.labelMapping = new LinkedHashMap<String,String>();
        for (String collectedLabel : collectedLabels) {
            this.labelMapping.put(collectedLabel, collectedLabel);
        }

        //create and ID mapping, initializing the to default "unchanged" values
        this.scmMapping = new LinkedHashMap<String,String>();
        for (String collectedScm : collectedScms) {
            this.scmMapping.put(collectedScm, collectedScm);
        }

    }

    /**
     * used by jelly in configureCloneTargets.jelly
     */
    public Set<String> getLabelMappingKeys() {
        return this.labelMapping.keySet();
    }

    /**
     * used by jelly in configureCloneTargets.jelly
     */
    public Set<String> getScmMappingKeys() {
        return scmMapping.keySet();
    }

    /**
     * this function initializes the members {@link #tabViewsToClone} and {@link #viewsSubgraphPropagtor}
     */
    private void determineViewsSubgraph() {

        //create a copy of the jenkins top tab views.
        this.tabViewsToClone = new ArrayList<View>(Jenkins.getInstance().getViews());

        //TODO: should be changed to work by instance of (can use CollectionUtils.filter and instanceof criteria)
        tabViewsToClone.remove(Jenkins.getInstance().getView("All")); //we're not going to clone the All view..

        //get the subgraph of paths leading to listviews with our cloned projects in them
        MapBasedPropagator<View> projectContainingSubgraphPropagator =
                new SubgraphPropagatorBuilder(
                        this.nestedViewsPropagator,
                        new ProjectContainmentDetectorHandler(this.jobsToClone),
                        tabViewsToClone).build();

        //keep only global views that lead to views containing one of our to be cloned projects.
        tabViewsToClone.retainAll(projectContainingSubgraphPropagator.getMap().keySet());
        this.viewsSubgraphPropagtor = projectContainingSubgraphPropagator;
    }

    /**
     *
     * @return the set of jobs that need to be cloned
     * (i.e. all jobs filtered according to the names of selected and propagated from user input)
     */
    private Set<Job> determineJobsToClone() {

        //TODO: create the hashset with the size parameter from both collections...
        Set<String> jobsToClone = new HashSet<String>(this.selectedPropagatedJobNames);
        jobsToClone.addAll(this.selectedJobNames);


        //TODO: use CollectionUtils.map with names and Jenkins.getInstance().getItemByFullName() instead, then delete both functions below used below
        Set<Job> sources = allJobs();
        filterJobsByName(sources, jobsToClone);

        return sources;
    }

    /**
     * used by configureCloneTargets.jelly
     */
    public Collection<View> getTabViewsToClone() {
        return tabViewsToClone;
    }

    /**
     *
     * this is the function that actually runs the clone logic, and is the final phase of the cloning flow.
     * all previous functions clonejobs flows redirected to jelly scripts and collected user data.
     * this function takes all the collected previously collected user input and applies the cloning procedure.
     * TODO: if we'll keep this plugin in its current form, the implementation of redirection logic etc should change to a state-machine for robustness.
     *
     * @param request containing the final user inputs
     *
     */
    public void doSaveCloneTargetConfiguration(StaplerRequest request, StaplerResponse response) throws ServletException, IOException {

        //if we somehow skipped the previous url... maybe a bookmark or an old session.
        if (this.selectedPropagatedJobNames == null) {
            response.sendRedirect(Jenkins.getInstance().getRootUrl() + this.getUrlName());
            return;
        }

        final JSONObject submittedForm = request.getSubmittedForm();

        //update the label mapping according to user input
        final Set<String> usedLabels = getLabelMappingKeys();
        int i = 0;
        for (String usedLabel: usedLabels) {
            //TODO: would be better not to rely on usedLabel, but on an index instead, for illegal string labels in html forms.
            String selectedLabel = (String) submittedForm.get(usedLabel + "SelectedLabel");
            this.labelMapping.put(usedLabel, selectedLabel);
            i++;
        }

        //update the scm mapping according to user input
        //TODO: same logic as above, very little difference, should be united in function
        final Set<String> usedScms = getScmMappingKeys();
        i = 0;
        for (String usedScm: usedScms) {

            //it's ok to use indexing in this way, because the collection type is LinkedHashSet, making the contained
            // element ordering consistent.
            String selectedScm = (String) submittedForm.get("" + i + "SelectedScm");
            this.scmMapping.put(usedScm, selectedScm);
            i++;
        }

        //create a jobTargets map from name of original job -> new name of cloned job
        final JSONObject selectedJobTargets = submittedForm.getJSONObject("jobTargets");
        Map<String,String> jobTargets = new HashMap<String,String>(this.jobsToClone.size());
        for (Job jobToClone : jobsToClone) {

            final String sourceJobName = "job" + jobToClone.getFullDisplayName();

            //TODO final String jobCustomWorkspace = "job" + jobToClone.getProperties();

            //TODO: use getString not get and cast to (String)
            String jobTarget = (String) (selectedJobTargets).get(sourceJobName);

            if (jobTarget == null) {
                throw FormValidation.error("could not get job target for " + sourceJobName);
            }

            this.validationHandler.validateJobName(jobTarget);

            jobTargets.put(jobToClone.getFullDisplayName(), jobTarget);

        }

        //read user inputs for the view names to create
        Map<View, String> tabViewMappings = new HashMap<View, String>();
        for (View clonedTabView : this.tabViewsToClone) {
            final String viewName = clonedTabView.getViewName();

            if (submittedForm.getBoolean("cv_" + viewName)) {
                tabViewMappings.put(clonedTabView, "" + submittedForm.get(viewName));
            }
        }

        //collection of view-generators
        Collection<ClonedProjectsViewGenerator> viewGenerators =
                new ArrayList<ClonedProjectsViewGenerator>();

        //this one will generate view hierarchically changing only the root view name
        viewGenerators.add(
                new TabbedViewsHierarchicCloner(
                    tabViewMappings,
                    this.viewsSubgraphPropagtor));

        //read the name of the single flat view containig all clone jobs TODO: getString()...?
        final String targetViewName = "" + submittedForm.get("targetViewName");

        //should a flat view containing the cloned jobs be created?
        Boolean makeTargetView = (Boolean) submittedForm.get("isBuildTargetView");

        if (makeTargetView) {

            viewGenerators.add(new TargetViewGenerator(targetViewName));
        }

        //combine the different view generators to a single view generator instance
        ClonedProjectsViewGenerator viewGenerator = CombinerFactory.combine(
                ClonedProjectsViewGenerator.class,
                viewGenerators,
                NullCombinationBuilder.getInstance());

        //combine the different job post processors to one post-processor.
        //TODO this should be extensible
        final Handler<?, Job> postJobCloneProcessor = CombinerFactory.combine(Handler.class,
                Arrays.asList(
                        new LabelChangingHandler(this.labelMapping),
                        new SvnScmChangingHandler(this.scmMapping),
                        //this should be included when using the xmlbasedjobscloner,
                        // and not when using the api based jobs cloner,
                        // because it saves in a different way
                        new SaveChangesHandler()
                ),
                NullCombinationBuilder.getInstance());

        //clone the jobs
        final Map<Job, Job> original2cloned =
                //new ApiBasedJobsCloner(postJobCloneProcessor).clone(jobTargets);
                new XmlBasedJobsCloner(postJobCloneProcessor).clone(jobTargets);


		//disable cloned jobs TODO: model as post job clone processor
        for (Map.Entry<Job, Job> entry : original2cloned.entrySet()) {
            //TODO if { if { should be if && instead
	        if ((selectedJobTargets).getBoolean("disableAfterCreate_" +  entry.getKey().getFullDisplayName())) {
	            if (entry.getValue() instanceof AbstractProject) {
		            final AbstractProject project = (AbstractProject) entry.getValue();
		            project.disable();
	            }
	        }
        }

        //generate the views
        viewGenerator.cloneViews(original2cloned);

        //redirect user to newly created all clone encompassing view if it exists or jenkins root.
        //TODO return user to url from which they originally came?
        String redirectUrl = Jenkins.getInstance().getRootUrl();
        if (makeTargetView) {
            redirectUrl += Jenkins.getInstance().getView(targetViewName).getUrl();
        }
        response.sendRedirect(redirectUrl);
    }


    //TODO remove this function, if need replacement use CollectionUtils.filter()
    private void filterJobsByName(Collection<Job> jobs, Set<String> jobsToClone) {

        final Iterator<Job> iterator = jobs.iterator();

        while (iterator.hasNext()) {

            final Job job = iterator.next();
            final String jobName = job.getFullDisplayName();

            if (!jobsToClone.contains(jobName)) {
                iterator.remove();
            }
        }
    }


    /**
     *
     * @return a collection of all jobs that are relevant to the cloning process e.g. are recursively contained in the
     * view, or propagated from a job that is recursively nsted in the view.
     */
    private Set<Job> allJobs() {

        //get a mapping of all selected jobs and their propagated jobs
        final Set<Map.Entry<Job, Collection<Job>>> entries = this.propagatedJobs.entrySet();

        //we need to create a collection as a response, now we calculate an upper bound on it's size for efficiency.

        //start by taking the number of jobs that exist anywhere in this view recursively.
        int jobsCount = this.hierarchicJobs.size();

        //then take the propagated jobs mapping
        for (Map.Entry<Job, Collection<Job>> entry : entries) {

            //and add 1 for the source job and the size of the propagted collection
            //TODO: 1 is unnecessary - it's already been counted in the hierarchicJobs
            jobsCount += 1 + entry.getValue().size();
        }

        //now actually build the collection
        final Set<Job> ret = new HashSet<Job>(jobsCount);
        ret.addAll(this.hierarchicJobs);
        for (Map.Entry<Job, Collection<Job>> entry : entries) {
            ret.addAll(entry.getValue());
            //TODO: again, this is redundant, already contained in hierarchicJobs.
            ret.add(entry.getKey());
        }

        return ret;
    }

    /**
     *
     * @return a collection of all projects contained in this view, including nested subviews.
     * matrix jobs will not contain their different configurations.
     */
    public Collection<? extends Job> getHierarchicJobs() {

        this.hierarchicJobs = new HashSet<Job>();

        this.viewsTraversal.setNodeHandler(new ViewTraversalJobNamesCollector(this.hierarchicJobs));
        this.viewsTraversal.apply(view);

        return this.hierarchicJobs;
    }


    /**
     * go over view hierarchically, and for each of of the selected jobs collect its propagated views.
     *
     * TODO: would be simpler to CollectionUtils.map(selectedJobs ...) with Jenkins.getInstance().getItemByFullName() as the iteration mechanism...
     */
    private void determinePropagatedJobs() {

        this.propagatedJobs = new HashMap<Job, Collection<Job>>();
        this.viewsTraversal.setNodeHandler(
                new ViewTraversalPropagatedJobsCollector(this.propagatedJobs, this.selectedJobNames));
        this.viewsTraversal.apply(view);
    }

    /**
     *
     * @param selectionCheckboxes mapping from jobname -> selected
     * @param selectedJobNames a collection where the selected jobs should be added
     * @throws ServletException
     *
     * TODO: use CollectionUtils.filter with MapValueCriteria instead of this function...
     */
    @SuppressWarnings("unchecked")
    private void saveSelectedJobs(Set<Map.Entry<String, Boolean>> selectionCheckboxes, Set<String> selectedJobNames) throws ServletException {

        for (Map.Entry<String, Boolean> checkbox : selectionCheckboxes) {

            final String jobName = checkbox.getKey();

            //ignore selectAll checkbox (submitted because of jenkins bug)
            if (jobName != null && !jobName.isEmpty()) {

                final Boolean selected = checkbox.getValue();
                if (selected) {

                    selectedJobNames.add(jobName);
                }
            }
        }
    }


}
