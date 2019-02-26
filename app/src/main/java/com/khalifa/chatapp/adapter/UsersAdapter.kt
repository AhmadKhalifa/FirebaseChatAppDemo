package com.khalifa.chatapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.khalifa.chatapp.R
import com.khalifa.chatapp.model.User
import kotlinx.android.synthetic.main.list_item_user.view.*

class UsersAdapter(val itemInteractionListener: OnItemInteractionListener?) :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var users: ArrayList<User>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false))

    override fun getItemCount() = users?.size ?: 0

    override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int) = viewHolder.setContent(this)

    class UsersViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setContent(usersAdapter: UsersAdapter) = with(view) {
            val user = usersAdapter.users?.get(adapterPosition)
            user?.run user@ {
                if ((imageUrl ?: "default") == "default") {
                    profileImageView.setImageResource(R.mipmap.ic_launcher_round)
                } else {
                    Glide.with(profileImageView.context).load(imageUrl).into(profileImageView)
                }
                usernameEditText.text = username
                view.setOnClickListener {
                    usersAdapter.itemInteractionListener?.onUserClick(this@user)
                }
            }
            Unit
        }
    }

    interface OnItemInteractionListener {

        fun onUserClick(user: User)
    }
}