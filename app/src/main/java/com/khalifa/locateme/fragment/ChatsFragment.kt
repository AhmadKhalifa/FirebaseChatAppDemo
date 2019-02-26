package com.khalifa.locateme.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.khalifa.locateme.R
import com.khalifa.locateme.activity.ChatActivity
import com.khalifa.locateme.adapter.UsersAdapter
import com.khalifa.locateme.model.Message
import com.khalifa.locateme.model.User
import kotlinx.android.synthetic.main.fragment_users.*
import java.lang.IllegalStateException

class ChatsFragment : Fragment(),
    UsersAdapter.OnItemInteractionListener {

    companion object {

        const val TITLE = "Chats"

        fun newInstance() = ChatsFragment()
    }

    private val chatsAdapter = UsersAdapter(this@ChatsFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = chatsAdapter
        }
        loadUsersIds()
    }

    private fun loadUsersIds() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersIdsSet = HashSet<String?>()
                dataSnapshot.children.forEach { it.getValue(Message::class.java)?.let { message ->
                    if (message.sender == firebaseUser?.uid) {
                        usersIdsSet.add(message.receiver)
                    }
                    if (message.receiver == firebaseUser?.uid) {
                        usersIdsSet.add(message.sender)
                    }
                }}
                loadUsers(usersIdsSet)
            }

        })
    }


    private fun loadUsers(usersIdsSet: HashSet<String?>) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = ArrayList<User>()
                dataSnapshot.children.forEach { it.getValue(User::class.java)?.let { user ->
                    if (user.id in usersIdsSet)
                        users.add(user)
                }}
                chatsAdapter.users = users
            }
        })
    }

    override fun onUserClick(user: User) = user.id?.run userId@ {
        ChatActivity.startActivity(activity, this@userId)
    } ?: throw IllegalStateException("Invalid user")
}
