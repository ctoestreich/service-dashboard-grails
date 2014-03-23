package com.bsb

import grails.transaction.Transactional

class SettingsService {

    def schedulingService

    @Transactional
    Settings save(Settings settings) {
        Settings existingSettings = Settings?.list()?.get(0)

        if (existingSettings.refreshTimer != settings.refreshTimer) {
            schedulingService.schedulePolling(settings.refreshTimer)
        }

        existingSettings.properties << settings.properties
        existingSettings.save(flush: true)
    }
}
