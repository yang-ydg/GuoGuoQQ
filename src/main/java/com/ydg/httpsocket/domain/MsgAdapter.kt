package com.ydg.httpsocket.domain

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R


class MsgAdapter(val msgList: List<Msg>,val friendHeadIcon:ByteArray,val myHeadIcon:ByteArray) : RecyclerView.Adapter<MsgViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Msg.TYPE_RECEIVED) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item, parent, false)
        LeftViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item, parent, false)
        RightViewHolder(view)
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is LeftViewHolder -> {
                holder.leftMsg.text = msg.content
                holder.friendHeadIcon.setImageBitmap(BitmapFactory.decodeByteArray(friendHeadIcon,0,friendHeadIcon.size))
            }
            is RightViewHolder -> {
                holder.rightMsg.text = msg.content
                holder.myHeadIcon.setImageBitmap(BitmapFactory.decodeByteArray(myHeadIcon,0,myHeadIcon.size))
            }
         }
    }

    override fun getItemCount() = msgList.size
}