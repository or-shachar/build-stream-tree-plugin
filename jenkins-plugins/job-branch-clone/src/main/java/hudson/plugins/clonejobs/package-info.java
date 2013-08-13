
/**
 * what does this plugin do?
 * it allows for fast cloning of multiple jobs, preserving their inter-connections and offering easy ways to replace
 * svn configurations and labels, making it very easy to clone coverage of one SCM branch to another with the click of a button.
 *
 * the clonejobs package contains all the classes that are unique to the clone jobs plugin.
 * generic behavior should be contained in the commons module, or the yet to be created commons-jenkins module.
 *
 * 2 classes can be regarded as starting points:
 * 1. {@link hudson.plugins.clonejobs.SidePanelAddition} which displays the clickable "Clone Jobs" link in jenkins ui.
 * 2. {@link hudson.plugins.clonejobs.CloneJobsAction} actually contains all the starting points and the url bindings for getting data from the ui.
 *
 * usage information:
 * on clicking the link "Clone Jobs" on the menu to the left you are redirected to a webpage where you can select the jobs you want to clone.
 * you are then redirected to a page offering the propagated jobs - jobs that the jobs you have selected are dependant on transitively.
 * the plugin traces the views containg the cloned jobs and offers you to make clones for these as well.
 * it also offers to create a "master" view containing all the cloned jobs.
 * you can map SCMs collected from all the jobs to other SCMs, and the same goes for labels.
 * finally you map via javascript regex matching the names of the original jobs to the new names you want the cloned jobs to have.
 * click clone, and wait a while... you're done.
 *
 * note, as of october 15th 2012 , the plugin doesn't help you with custom workspaces... BLI?
*/
package hudson.plugins.clonejobs;