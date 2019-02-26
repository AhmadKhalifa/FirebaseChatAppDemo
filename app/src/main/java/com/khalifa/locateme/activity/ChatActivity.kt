package com.khalifa.locateme.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khalifa.locateme.R
import com.khalifa.locateme.adapter.MessagesAdapter
import com.khalifa.locateme.fragment.TimePickerFragment
import com.khalifa.locateme.model.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.IllegalStateException

private const val KEY_USER_ID = "com.khalifa.chatapp.activity.ChatActivity.KEY_USER_ID"

class ChatActivity : AppCompatActivity() {

    companion object {

        fun startActivity(activity: Activity?, userId: String) = activity?.run {
            startActivity(
                Intent(this, ChatActivity::class.java).apply { putExtra(KEY_USER_ID, userId) }
            )
        }
    }

    private lateinit var userId: String
    private lateinit var currentUser: FirebaseUser
    private lateinit var otherUser: User

    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
        messagesRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply { stackFromEnd = true }
            itemAnimator = DefaultItemAnimator()
        }
        userId = intent.getStringExtra(KEY_USER_ID) ?: throw IllegalStateException("Invalid user id")
        currentUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.run user@ {
                    otherUser = this@user
                    usernameEditText.text = username
                    if ((imageUrl ?: "default") == "default") {
                        profileImageView.setImageResource(R.mipmap.ic_launcher_round)
                    } else {
                        Glide.with(this@ChatActivity).load(imageUrl).into(profileImageView)
                    }
                    if (!::messagesAdapter.isInitialized) {
                        messagesAdapter = MessagesAdapter(userId, imageUrl)
                    }
                    messagesRecyclerView.adapter = messagesAdapter
                    val tracked = isTracked
                    trackingButton.text = "${ if (tracked) "Stop" else "Start"} tracking"
                    readMessages()
                }
            }
        })
        trackingButton.setOnClickListener {
            if (otherUser.isTracked) {
                setTracked(false)
            } else {
                setTracked(true)
            }
        }
        changeIntervalButton.setOnClickListener {
            TimePickerFragment.showFragment(
                supportFragmentManager,
                object : TimePickerFragment.OnFragmentInteractionListener {
                    override fun onTimeSet(hours: Int, minutes: Int) = setInterval(hours, minutes)
                })
        }
    }

    private fun setTracked(tracked: Boolean) {
        FirebaseDatabase.getInstance().getReference("Users").child(userId).setValue(
            otherUser.apply { isTracked = tracked }
        ).addOnCompleteListener { databaseTask ->
            if (databaseTask.isSuccessful) {
                sendMessage(Message(
                    sender = currentUser.uid,
                    receiver = userId,
                    message = "Tracking ${ if (tracked) "started" else "stopped"}",
                    type = if (tracked) TYPE_START_TRACKING else TYPE_STOP_TRACKING
                ))
            }
        }
    }

    private fun setInterval(hours: Int, minutes: Int) {
        FirebaseDatabase.getInstance().getReference("Users").child(userId).setValue(
            otherUser.apply { isTracked = true }.apply { interval = "$hours:$minutes" }
        ).addOnCompleteListener { databaseTask ->
            if (databaseTask.isSuccessful) {
                sendMessage(Message(
                    sender = currentUser.uid,
                    receiver = userId,
                    message = "Location will be sent every $hours hours and $minutes minutes",
                    type = TYPE_INTERVAL_UPDATE
                ))
            }
        }
    }

    private fun sendMessage(message: Message) =
        FirebaseDatabase.getInstance().reference.child("Chats").push().setValue(message)

    private fun readMessages() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val messages = ArrayList<Message>()
                dataSnapshot.children.forEach {
                    it.getValue(Message::class.java)?.let { message ->
                        if (message.receiver in arrayOf(currentUser.uid, userId) &&
                            message.sender in arrayOf(currentUser.uid, userId)
                        ) {
                            messages.add(message)
                        }
                    }
                }
                messagesAdapter.messages = messages
            }
        })
    }
}
