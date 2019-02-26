package com.khalifa.locateme.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.khalifa.locateme.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class LoginActivity : AppCompatActivity() {

    companion object {

        fun startActivity(activity: Activity?) = activity?.run {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        actionBar?.run {
            setTitle(R.string.login)
            setDisplayHomeAsUpEnabled(true)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener { login() }
    }

    private fun login() {
        var allFieldsFilled = true
        val email = emailEditText.text?.toString() ?: ""
        val password = passwordEditText.text?.toString() ?: ""
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = getString(R.string.required_field)
            allFieldsFilled = false
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = getString(R.string.required_field)
            allFieldsFilled = false
        }
        if (allFieldsFilled) {
            firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    HomeActivity.startActivity(this@LoginActivity)
                } else {
                    Toast.makeText(this@LoginActivity, R.string.invalid_login_message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
