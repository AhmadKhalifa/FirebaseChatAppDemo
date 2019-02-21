package com.khalifa.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.khalifa.chatapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    companion object {

        const val SPLASH_DELAY_MS = 1000L

        fun startActivity(activity: Activity?) = activity?.run {
            startActivity(
                Intent(this, SplashActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(::checkUser, SPLASH_DELAY_MS)
    }

    private fun checkUser() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            HomeActivity.startActivity(this@SplashActivity)
        } else {
            loginButton.setOnClickListener { LoginActivity.startActivity(this@SplashActivity) }
            registerButton.setOnClickListener {
                RegisterActivity.startActivity(
                    this@SplashActivity
                )
            }
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
        }
    }
}
