package com.khalifa.locateme.model

data class User(var id: String?,
                var username: String?,
                var imageUrl: String?,
                var isTracked: Boolean = false,
                var interval: String = "0:0") {

    constructor() : this(null, null, null)
}