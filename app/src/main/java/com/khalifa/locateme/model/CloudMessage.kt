package com.khalifa.locateme.model

const val TYPE_UNKNOWN = 0
const val TYPE_LOCATION_UPDATE = 1
const val TYPE_INTERVAL_UPDATE = 2
const val TYPE_START_TRACKING = 3
const val TYPE_STOP_TRACKING = 4

data class CloudMessage(var sender: String?,
                        var receiver: String?,
                        val messageType: Int = TYPE_UNKNOWN,
                        val messageBody: String?,
                        val latitude: Double? = 0.0,
                        val longitude: Double? = 0.0,
                        val intervalHours: Int? = 1,
                        val intervalMinutes: Int? = 0) {
    constructor() : this(null, null, TYPE_UNKNOWN, null)
}
