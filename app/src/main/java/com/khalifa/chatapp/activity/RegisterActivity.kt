package com.khalifa.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khalifa.chatapp.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class RegisterActivity : AppCompatActivity() {

    companion object {

        fun startActivity(activity: Activity?) = activity?.run {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        actionBar?.run {
            setTitle(R.string.register)
            setDisplayHomeAsUpEnabled(true)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        signUpButton.setOnClickListener{ register() }
    }

    private fun register() {
        var allFieldsFilled = true
        val username = usernameEditText.text?.toString() ?: ""
        val email = emailEditText.text?.toString() ?: ""
        val password = passwordEditText.text?.toString() ?: ""
        if (TextUtils.isEmpty(username)) {
            usernameEditText.error = getString(R.string.required_field)
            allFieldsFilled = false
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = getString(R.string.required_field)
            allFieldsFilled = false
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = getString(R.string.required_field)
            allFieldsFilled = false
        }
        if (allFieldsFilled) {
            firebaseAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth?.currentUser
                    val userId = firebaseUser?.uid
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId ?: "")
                    databaseReference?.setValue(
                        hashMapOf(
                            "id" to userId,
                            "username" to username,
                            "imageUrl" to "default"
                        )
                    )?.addOnCompleteListener { databaseTask ->
                        if (databaseTask.isSuccessful) {
                            HomeActivity.startActivity(this@RegisterActivity)
                        }
                    }
                }
            }
        }
    }
}
