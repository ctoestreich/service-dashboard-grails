package com.bsb

import grails.converters.JSON
import grails.plugin.cache.Cacheable

class DataController {

//    @Cacheable(value = "serviceTypesCache")
    def serviceTypes() {
        render ServiceType.values() as JSON
    }

//    @Cacheable(value = "environmentTypesCache")
    def environmentTypes() {
        render EnvironmentType.values() as JSON
    }
}
