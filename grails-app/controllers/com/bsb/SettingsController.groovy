package com.bsb

import grails.rest.RestfulController

class SettingsController extends RestfulController {

    def settingsService

    static responseFormats = ['json', 'xml']

    SettingsController() {
        super(Settings)
    }

    def index(){
        respond Settings?.list()?.get(0), model:[settingsCount: Settings.count()]
    }

    def save(Settings settings) {
        respond settingsService.save(settings)
    }
}
