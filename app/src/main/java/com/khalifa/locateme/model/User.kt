package com.khalifa.locateme.model

data class User(var id: String?, var username: String?, var imageUrl: String?) {

    constructor() : this(null, null, null)
}