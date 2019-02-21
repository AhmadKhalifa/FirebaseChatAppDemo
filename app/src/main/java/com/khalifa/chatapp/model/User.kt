package com.khalifa.chatapp.model

data class User(var id: String?, var username: String?, var imageUrl: String?) {

    constructor() : this(null, null, null)
}