package com.khalifa.chatapp.model

data class Message(var sender: String?, var receiver: String?, var message: String?) {

    constructor() : this(null, null, null)
}