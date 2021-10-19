package com.ydg.httpsocket.domain

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R
import com.ydg.httpsocket.databinding.FriendsItemBinding

class FriendsRecyclerAdapter(val friendsList: List<User>,val activity: Activity):RecyclerView.Adapter<FriendsRecyclerAdapter.FriendsViewHolder>() {
    private lateinit var binding: FriendsItemBinding
    private var listener : OnItemClickListener? = null

    inner class FriendsViewHolder(view : View):RecyclerView.ViewHolder(view){
        val chatHeadIcon : ImageView = binding.chatHeadIcon
        val chatLastMsg : TextView = binding.chatLastMsg
        val chatNameText : TextView = binding.chatNameText
    }

    //点击外调
    interface OnItemClickListener{
        fun onClick(holder: FriendsViewHolder)
    }
    fun addOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.listener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
       binding = FriendsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = FriendsViewHolder(binding.root)
        holder.itemView.setOnClickListener {
           listener?.onClick(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val friend = friendsList[position]
        holder.apply {
            chatHeadIcon.setImageBitmap(BitmapFactory.decodeByteArray(friend.headIcon,0, friend.headIcon!!.size))
            chatLastMsg.text = friend.perSign
            chatNameText.text = friend.username
        }
    }

    override fun getItemCount() = friendsList.size

}