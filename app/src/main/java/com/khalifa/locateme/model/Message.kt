package com.khalifa.locateme.model

const val TYPE_UNKNOWN = 0
const val TYPE_LOCATION_UPDATE = 1
const val TYPE_INTERVAL_UPDATE = 2
const val TYPE_START_TRACKING = 3
const val TYPE_STOP_TRACKING = 4

data class Message(var type: Int, var sender: String?, var receiver: String?, var message: String?) {

    constructor() : this(TYPE_UNKNOWN, null, null, null)
}