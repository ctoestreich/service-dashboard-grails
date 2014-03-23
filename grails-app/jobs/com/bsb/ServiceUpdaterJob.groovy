package com.bsb
/**
 * Created by christian.oestreich on 3/18/14.
 */
class ServiceUpdaterJob {

    def serviceEndpointStatusService

    static triggers = {
        simple name: 'serviceEndpointStatusTrigger', repeatInterval: 120000, startDelay: 1000
    }

    def execute() {
        println "Running status update ${new Date()}"
        ServiceEndpoint.list().each { ServiceEndpoint serviceEndpoint ->
            log.info "Updating endpoint ${serviceEndpoint.endpointName}"
            serviceEndpointStatusService.refresh(serviceEndpoint)
        }
    }
}
