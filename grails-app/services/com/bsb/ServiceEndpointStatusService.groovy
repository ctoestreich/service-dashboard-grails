package com.bsb

import grails.transaction.Transactional
import groovy.transform.Synchronized

@Transactional
class ServiceEndpointStatusService {

    def pollingService

    ServiceEndpointStatus retrieve(ServiceEndpoint serviceEndpoint) {
        if(serviceEndpoint) {
            ServiceEndpointStatus serviceEndpointStatus = pollingService.getGetServiceEndpointStatus(serviceEndpoint)
            serviceEndpointStatus.serviceEndpoint = serviceEndpoint
            serviceEndpointStatus.save(failOnError: true)
        }
    }

    @Synchronized
    ServiceEndpointStatus status(ServiceEndpoint serviceEndpoint) {
        ServiceEndpointStatus serviceEndpointStatus = serviceEndpoint?.serviceEndpointStatus
        if(!serviceEndpointStatus){
            serviceEndpointStatus = refresh(serviceEndpoint)
        }
        serviceEndpointStatus
    }

    @Synchronized
    ServiceEndpointStatus refresh(ServiceEndpoint serviceEndpoint) {
        retrieve(serviceEndpoint)
    }
}
