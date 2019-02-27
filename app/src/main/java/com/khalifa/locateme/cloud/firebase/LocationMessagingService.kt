package com.khalifa.locateme.cloud.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.khalifa.locateme.model.*
import com.khalifa.locateme.util.CloudMessageHandler

class LocationMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val receiverId = remoteMessage?.data?.get("receiver")
        if (receiverId == FirebaseAuth.getInstance().currentUser?.uid) {
            val message = CloudMessage(
                sender = remoteMessage?.data?.get("sender"),
                receiver = remoteMessage?.data?.get("receiver"),
                messageBody = remoteMessage?.data?.get("messageBody"),
                messageType = remoteMessage?.data?.get("messageType")?.toInt() ?: TYPE_UNKNOWN,
                latitude = remoteMessage?.data?.get("latitude")?.toDouble(),
                longitude = remoteMessage?.data?.get("longitude")?.toDouble(),
                intervalHours = remoteMessage?.data?.get("intervalHours")?.toInt(),
                intervalMinutes = remoteMessage?.data?.get("intervalMinutes")?.toInt()
            )
            message.run {
                when(messageType) {
                    TYPE_LOCATION_UPDATE -> { CloudMessageHandler.onLocationRetrieved(sender, latitude, longitude) }
                    TYPE_INTERVAL_UPDATE -> { CloudMessageHandler.onIntervalChanged(intervalHours, intervalMinutes) }
                    TYPE_START_TRACKING -> { CloudMessageHandler.onTrackingStarted(intervalHours, intervalMinutes) }
                    TYPE_STOP_TRACKING -> { CloudMessageHandler.onTrackingStopped() }
                }
            }
        }
    }
}