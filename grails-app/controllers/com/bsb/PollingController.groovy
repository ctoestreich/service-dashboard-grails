package com.bsb

import grails.converters.JSON

class PollingController {

    def serviceEndpointStatusService

    def status() {
        def id = params.int("id") ?: 0
        def result = [success: false, up: false, down: false, exception: "", lastResponse: ""]
        if (id > 0) {
            result = serviceEndpointStatusService.status(ServiceEndpoint.get(id))
        }

        render (result ?: {}) as JSON
    }

    def refresh() {
        def id = params.int("id") ?: 0
        def result = [success: false, up: false, down: false, exception: "", lastResponse: ""]
        if (id > 0) {
            result = serviceEndpointStatusService.refresh(ServiceEndpoint.get(id))
        }

        render result as JSON
    }
}
