package com.ydg.httpsocket.domain


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R
import com.ydg.httpsocket.databinding.ChatItemBinding

class ChatAdapter(val chatList: List<User>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private lateinit var  binding:ChatItemBinding
    private var listener : OnItemClickListener? = null
    private var longListener : OnItemLongClickListener? = null
    inner class ChatViewHolder(view : View):RecyclerView.ViewHolder(view){
        val chatHeadIcon : ImageView = binding.chatHeadIcon
        val chatLastMsg : TextView = binding.chatLastMsg
        val chatNameText : TextView = binding.chatNameText
    }

    //接口回调
    interface OnItemClickListener{
        fun click(holder: ChatViewHolder)
    }
    interface OnItemLongClickListener{
        fun longClick(holder: ChatViewHolder)
    }

    fun addOnItemClickListener(onItemClickListener:OnItemClickListener){
        this.listener = onItemClickListener
    }
    fun addOnItenLongClickListener(onItemLongClickListener: OnItemLongClickListener){
        this.longListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder : ChatViewHolder = ChatViewHolder(binding.root)
        holder.itemView.setOnClickListener {
            listener?.click(holder)
        }
        holder.itemView.setOnLongClickListener{
            longListener?.longClick(holder)
           true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.chatHeadIcon.setImageBitmap(BitmapFactory.decodeByteArray(chat.headIcon,0, chat.headIcon?.size ?: 0))
        holder.chatNameText.text = chat.username
        holder.chatLastMsg.text = chat.perSign
    }

    override fun getItemCount() = chatList.size

}