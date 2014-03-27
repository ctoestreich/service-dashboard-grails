package com.bsb

import grails.converters.JSON

class DataController {

    def serviceTypes() {
        render ServiceType.values() as JSON
    }

    def environmentTypes() {
        render EnvironmentType.values() as JSON
    }
}
