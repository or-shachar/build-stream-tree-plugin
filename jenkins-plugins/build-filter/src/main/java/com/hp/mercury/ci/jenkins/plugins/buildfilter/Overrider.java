package com.hp.mercury.ci.jenkins.plugins.buildfilter;

import hudson.model.Job;
import hudson.widgets.BuildHistoryWidget;
import hudson.widgets.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: grunzwei
 * Date: 24/06/13
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
public class Overrider {

    private final BranchAwareBuildHistoryWidget widget;
    private final Job job;

    public Overrider(BranchAwareBuildHistoryWidget widget, Job job) {

        this.job = job;
        this.widget = widget;
    }

    public List<Widget> getWidgets() {

        ArrayList<Widget> r = new ArrayList<Widget>();

        final List<Widget> widgets = job.getWidgets();
        for (Widget w : widgets)
            r.add(w instanceof BuildHistoryWidget ? widget : w);

        return r;
    }

    public Job getDelegatedJob() {
        return job;
    }

    //when accessing buildHistory url of job this class will be used by stapler metaclass dispatchers and getOverrides()
    // getJobOverrides() methods and this method will be called instead of getWidgets() of Job, where we return THIS
    // history widget... muwahahaha!!
    @SuppressWarnings("unused")
    public Widget getBuildHistory() {

        return widget;
    }
}
