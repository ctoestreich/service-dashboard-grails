package com.bsb

import grails.rest.RestfulController
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ServiceEndpointController extends RestfulController {

    static responseFormats = ['json', 'xml']

    def serviceEndpointStatusService

    ServiceEndpointController() {
        super(ServiceEndpoint)
    }

    def index(){
        respond ServiceEndpoint.list(), model:[serviceEndpointCount: ServiceEndpoint.count()]
    }

    def save(ServiceEndpoint serviceEndpoint) {
        if (serviceEndpoint.validate()) {
            serviceEndpoint.save(flush: true)
            serviceEndpointStatusService.refresh(serviceEndpoint)
        }

        respond serviceEndpoint, model: [serviceEndpoint: serviceEndpoint]
    }

    def status(ServiceEndpoint serviceEndpoint){
        respond serviceEndpoint.serviceEndpointStatus, model: [serviceEndpointStatus: serviceEndpoint.serviceEndpointStatus]
    }

}
