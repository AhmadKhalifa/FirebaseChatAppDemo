package com.khalifa.locateme.cloud.payload

import com.khalifa.locateme.model.CloudMessage

data class MessagePayload(val data: CloudMessage, val to: String)