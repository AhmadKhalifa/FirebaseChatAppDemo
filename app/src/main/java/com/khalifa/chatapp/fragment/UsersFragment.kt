package com.khalifa.chatapp.fragment

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
import com.khalifa.chatapp.R
import com.khalifa.chatapp.adapter.UsersAdapter
import com.khalifa.chatapp.model.User
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment(), UsersAdapter.OnItemInteractionListener {

    companion object {

        const val TITLE = "Users"

        fun newInstance() = UsersFragment()
    }

    private val usersAdapter = UsersAdapter(this@UsersFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = usersAdapter
        }
        loadUsers()
    }

    private fun loadUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = ArrayList<User>()
                dataSnapshot.children.forEach { it.getValue(User::class.java)?.let { user ->
                    if (user.id != firebaseUser?.uid)
                        users.add(user)
                }}
                usersAdapter.users = users
            }

        })
    }

    override fun onUserClick(user: User) {

    }
}
