package com.ydg.httpsocket.domain

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R

sealed class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view)

class LeftViewHolder(view: View) : MsgViewHolder(view) {
    val leftMsg: TextView = view.findViewById(R.id.leftMsg)
    val friendHeadIcon : ImageView = view.findViewById(R.id.friendHeadIcon)
}

class RightViewHolder(view: View) : MsgViewHolder(view) {
    val rightMsg: TextView = view.findViewById(R.id.rightMsg)
    val myHeadIcon :ImageView = view.findViewById(R.id.myHeadIcon)
}