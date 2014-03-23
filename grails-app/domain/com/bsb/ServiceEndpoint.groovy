package com.bsb

import grails.rest.Resource
import groovy.transform.ToString

//@Resource(uri = '/api/endpoints', formats = ['json', 'xml'])
@ToString(includeFields = true, includePackage = false, includeNames = true)
class ServiceEndpoint {

    static hasOne = [serviceEndpointStatus: ServiceEndpointStatus]

    String endpointName
    ServiceType serviceType = ServiceType.SOAP
    EnvironmentType environmentType = EnvironmentType.DEV
    String soapAction
    String requestUrl
    String requestBody
    String successNode
    String successValue
    Integer pollIntervalMilliSeconds = 30000
    ServiceEndpointStatus serviceEndpointStatus = new ServiceEndpointStatus()

    static constraints = {
        endpointName blank: false, nullable: false
        requestUrl blank: false, nullable: false
        requestBody blank: true, nullable: true
        serviceType nullable: false
        pollIntervalMilliSeconds nullable: false, min: 5000
        soapAction nullable: true
        successNode nullable: true
        successValue nullable: true
        environmentType nullable: false
        serviceEndpointStatus nullable: true
    }

    static mapping = {
        sort "endpointName"
        requestBody type: 'text'
    }
}
