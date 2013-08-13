package com.hp.mercury.ci.jenkins.plugins.abstractpipeline.PipelineViewExtension

import hudson.model.Items
import hudson.model.Job
import jenkins.model.Jenkins
import org.joda.time.Duration
import org.joda.time.Instant
import org.joda.time.Interval
import org.joda.time.format.PeriodFormatterBuilder

def l = namespace(lib.LayoutTagLib)
l.html {
l.link( href:"$rootURL/plugin/abstract-pipeline/css/pipeline.css",
        rel:"stylesheet",
        type:"text/css")

l.meta("http-equiv":"Refresh", content:"1")

def formatter = new PeriodFormatterBuilder()
        .appendDays()
        .appendSuffix("d")
        .appendHours()
        .appendSuffix("h")
        .appendMinutes()
        .appendSuffix("m")
        .appendSeconds()
        .appendSuffix("s")
        .toFormatter();

l.div(class:"pipeline-container") {

    my.segments.each {

        def segment = it
        def jobs = Items.fromNameList(
                Jenkins.instance,
                segment.jobsContainedInSegment,
                Job.class)

        def active = []
        def duration = []
        def average = []

        jobs.each {
            def job = it
            if (job.building || job.inQueue) {

                active += job.fullDisplayName
                average += formatter.print(new Duration(job.estimatedDuration).toPeriod())

                if (job.building) {
                    def start = job.building ? new Instant(job.lastBuild.timeInMillis) : new Instant()
                    def interval = new Interval(start, new Instant())
                    duration += formatter.print(interval.toDuration().toPeriod())
                }

                else {
                    duration += "(queued)"
                }
            }
        }

        l.div(class: "${active.empty ? "" : "pipeline-active"} pipeline-segment") {
            l.div(class: "pipeline-segment-text") {

                l.text(segment.segmentName)
                l.br()
                l.table (width:"100%"){

                    l.tr {
                        l.th {
                            l.text("Phase")
                        }
                        l.th {
                            l.text("Time")
                        }
                        l.th {
                            l.text("Avg")
                        }
                    }

                    [active, duration, average].transpose().each {
                        def pair = it
                        l.tr {
                            l.td {
                                l.text(pair[0].toString())
                            }
                            l.td {
                                l.text(pair[1].toString())
                            }
                            l.td {
                                l.text(pair[2].toString())
                            }
                        }
                    }
                }
            }
        }

        l.div(class: "pipeline-separator")
    }
}

}
