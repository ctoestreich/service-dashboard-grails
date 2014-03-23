package com.bsb

import org.quartz.Trigger
import org.quartz.TriggerKey
import org.quartz.impl.triggers.SimpleTriggerImpl

class SchedulingService {

    public static final String SERVICE_ENDPOINT_STATUS_TRIGGER = 'serviceEndpointStatusTrigger'
    public static final String GRAILS_JOBS = 'GRAILS_JOBS'
    def jobManagerService

    void schedulePolling(long milliseconds) {

        TriggerKey triggerKey = TriggerKey.triggerKey(SERVICE_ENDPOINT_STATUS_TRIGGER, GRAILS_JOBS)
        jobManagerService.unscheduleJob(GRAILS_JOBS, ServiceUpdaterJob.name)
        jobManagerService.quartzScheduler.unscheduleJob(triggerKey)

        Trigger newTrigger = new SimpleTriggerImpl()
        newTrigger.setJobGroup(GRAILS_JOBS)
        newTrigger.setJobName(ServiceUpdaterJob.name)
        newTrigger.setRepeatInterval(milliseconds)
        newTrigger.setName(SERVICE_ENDPOINT_STATUS_TRIGGER)
        newTrigger.setStartTime(new Date())
        newTrigger.setRepeatCount(-1)

        jobManagerService.quartzScheduler.scheduleJob(newTrigger)
    }
}
