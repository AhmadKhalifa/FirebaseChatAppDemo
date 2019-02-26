package com.khalifa.chatapp.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khalifa.chatapp.R
import com.khalifa.chatapp.adapter.MessagesAdapter
import com.khalifa.chatapp.model.Message
import com.khalifa.chatapp.model.User
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
    private var databaseReference: DatabaseReference? = null

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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.run {
                    usernameEditText.text = username
                    if ((imageUrl ?: "default") == "default") {
                        profileImageView.setImageResource(R.mipmap.ic_launcher_round)
                    } else {
                        Glide.with(this@ChatActivity).load(imageUrl).into(profileImageView)
                    }
                    messagesAdapter = MessagesAdapter(id!!, imageUrl)
                    messagesRecyclerView.adapter = messagesAdapter
                    readMessages()
                }
            }
        })
        sendImageButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        messageEditText.text?.toString()?.let { message ->
            if (!TextUtils.isEmpty(message)) {
                val databaseReference = FirebaseDatabase.getInstance().reference
                databaseReference.child("Chats").push().setValue(hashMapOf(
                    "sender" to currentUser.uid,
                    "receiver" to userId,
                    "message" to message
                ))
                messageEditText.setText("")
            }
        }
    }

    private fun readMessages() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val messages = ArrayList<Message>()
                dataSnapshot.children.forEach { it.getValue(Message::class.java)?.let { message ->
                    if (message.receiver in arrayOf(currentUser.uid, userId) &&
                            message.sender in arrayOf(currentUser.uid, userId)) {
                        messages.add(message)
                    }
                }}
                messagesAdapter.messages = messages
            }
        })
    }
}
