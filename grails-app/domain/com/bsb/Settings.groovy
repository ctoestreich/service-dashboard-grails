package com.bsb


class Settings {
    static final int DEFAULT_CONNECTION_TIMEOUT = 30000
    static final int DEFAULT_READ_TIMEOUT = 30000
    static final int DEFAULT_REFRESH_TIMER_INTERVAL = 120000


    long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT
    long readTimeout = DEFAULT_READ_TIMEOUT
    long refreshTimer = DEFAULT_REFRESH_TIMER_INTERVAL

    static long getGlobalConnectionTimeout() {
        findAll()?.get(0)?.connectionTimeout ?: DEFAULT_CONNECTION_TIMEOUT
    }

    static long getGlobalReadTimeout() {
        findAll()?.get(0)?.readTimeout ?: DEFAULT_READ_TIMEOUT
    }

    static long getGlobalRefreshTimer() {
        findAll()?.get(0)?.refreshTimer ?: DEFAULT_REFRESH_TIMER_INTERVAL
    }
}
