package com.ydg.httpsocket.domain

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R

class GroupRecyclerAdapter(val pageList:List<GroupItem>,val activity: Activity) : RecyclerView.Adapter<GroupRecyclerAdapter.GroupRecyclerViewHolder>(){
    private var listener :OnItemClickListener? = null
    inner class  GroupRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view){
        val pageText:TextView = view.findViewById(R.id.pageText)
        val pageItemBackground : LinearLayout = view.findViewById(R.id.pageItemBackground)
    }

    //接口回调
    interface OnItemClickListener{
        fun onClick(holder: GroupRecyclerViewHolder)
    }
    fun addOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.listener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.page_item,parent,false)
        var holder = GroupRecyclerViewHolder(view)
        holder.itemView.setOnClickListener {
            listener?.onClick(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: GroupRecyclerViewHolder, position: Int) {
        if (pageList[position].selected) {
            holder.pageText.setTextColor(Color.parseColor("#03C3F7"))
            holder.pageItemBackground.background = activity.getDrawable(R.drawable.pageitem_bg)
        }else{
            holder.pageText.setTextColor(Color.parseColor("#A6A8B5"))
            holder.pageItemBackground.background = activity.getDrawable(R.color.white)
        }
        val page = pageList[position]
        holder.pageText.text = page.itemName
    }

    override fun getItemCount()=pageList.size
}