package com.khalifa.locateme.cloud.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class LocationFirebaseService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        currentUser?.run { updateToken(refreshToken) }
    }

    private fun updateToken(token: String?) = token?.run {
        FirebaseDatabase
            .getInstance()
            .getReference("Tokens")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(token)
    }
}