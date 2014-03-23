package com.bsb

import com.bsb.ServiceEndpoint
import grails.rest.Resource

@Resource(uri = '/api/endpoints/status', formats = ['json', 'xml'])
class ServiceEndpointStatus {

    static belongsTo = [serviceEndpoint: ServiceEndpoint]

    boolean up = true
    boolean down = false
    boolean success = false
    String exception = ""
    String lastResponse = ""
    Date lastPollingDate = new Date()

    static constraints = {
        exception nullable: true, blank: true
        lastResponse nullable: true, blank: true
    }

    static mapping = {
        autoTimestamp(true)
        lastResponse type: 'text'
        exception type: 'text'
    }
}
