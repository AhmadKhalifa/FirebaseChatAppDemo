package com.khalifa.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khalifa.chatapp.R
import com.khalifa.chatapp.adapter.ViewPagerAdapter
import com.khalifa.chatapp.fragment.ChatsFragment
import com.khalifa.chatapp.fragment.UsersFragment
import com.khalifa.chatapp.model.User
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {

        fun startActivity(activity: Activity?) = activity?.run {
            startActivity(
                Intent(this, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            finish()
        }
    }

    private var currentUser: FirebaseUser? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewpager.adapter = viewPagerAdapter.apply {
            addFragment(ChatsFragment.newInstance(), ChatsFragment.TITLE)
            addFragment(UsersFragment.newInstance(), UsersFragment.TITLE)
        }
        tabLayout.setupWithViewPager(viewpager)
        currentUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser?.uid!!)
        databaseReference?.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                usernameEditText.text = user?.username
                if ((user?.imageUrl ?: "default") == "default") {
                    profileImageView.setImageResource(R.mipmap.ic_launcher_round)
                } else {
                    Glide.with(this@HomeActivity).load(user?.imageUrl).into(profileImageView)
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_logout -> {
            logout()
            true
        }
        else -> false
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        SplashActivity.startActivity(this@HomeActivity)
    }
}
