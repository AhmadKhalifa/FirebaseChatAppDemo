package com.khalifa.chatapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.khalifa.chatapp.R
import com.khalifa.chatapp.model.Message

private const val MESSAGE_TYPE_LEFT = 0
private const val MESSAGE_TYPE_RIGHT = 1

class MessagesAdapter(private val otherUserId: String,
                      private val otherUserProfileUrl: String?)
    : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    var messages: ArrayList<Message>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessagesViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(
            if (viewType == MESSAGE_TYPE_LEFT)
                R.layout.chat_item_left
            else
                R.layout.chat_item_right,
            parent,
            false
        ),
        viewType
    )

    override fun onBindViewHolder(viewHolder: MessagesViewHolder, position: Int) = viewHolder.setContent(this)

    override fun getItemCount() = messages?.size ?: 0

    override fun getItemViewType(position: Int) =
        if(messages!![position].sender == otherUserId)
            MESSAGE_TYPE_LEFT
        else
            MESSAGE_TYPE_RIGHT

    class MessagesViewHolder(private val view: View, private val viewType: Int) : RecyclerView.ViewHolder(view) {
        fun setContent(messagesAdapter: MessagesAdapter) = with(view) view@ {
            val message = messagesAdapter.messages?.get(adapterPosition)
            message?.run message@ {
                this@view.findViewById<TextView>(R.id.messageTextView).text = this@message.message
                if (viewType == MESSAGE_TYPE_LEFT) {
                    val profileImageView = this@view.findViewById<ImageView>(R.id.profileImageView)
                    if ((messagesAdapter.otherUserProfileUrl ?: "default") == "default") {
                        profileImageView.setImageResource(R.mipmap.ic_launcher_round)
                    } else {
                        Glide
                            .with(profileImageView.context)
                            .load(messagesAdapter.otherUserProfileUrl)
                            .into(profileImageView)
                    }
                }
            }
            Unit
        }
    }
}