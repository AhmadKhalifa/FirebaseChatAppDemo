package com.khalifa.locateme.util

import android.util.Log

private val TAG = CloudMessageHandler::class.java.simpleName

class CloudMessageHandler private constructor() {

    companion object {

        fun onLocationRetrieved(userId: String?, latitude: Double?, longitude: Double?) {
            Log.i(TAG, "onLocationRetrieved")
        }

        fun onIntervalChanged(hours: Int?, minutes: Int?) {
            Log.i(TAG, "onIntervalChanged")
        }

        fun onTrackingStarted(hours: Int? = 1, minutes: Int? = 0) {
            Log.i(TAG, "onTrackingStarted")
        }

        fun onTrackingStopped() {
            Log.i(TAG, "onTrackingStopped")
        }
    }
}