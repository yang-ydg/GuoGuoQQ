package com.ydg.httpsocket.domain

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R

class CardSelectedAdapter(var selectedList : List<ByteArray>) : RecyclerView.Adapter<CardSelectedAdapter.SelectedVH>(){
    inner class SelectedVH(view: View) : RecyclerView.ViewHolder(view){
            val selectedItem : ImageView = view.findViewById(R.id.card_selected_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardselected_item,parent,false)
        return SelectedVH(view)
    }

    override fun onBindViewHolder(holder: SelectedVH, position: Int) {
        val selectedItem = selectedList[position]
        holder.selectedItem.setImageBitmap(BitmapFactory.decodeByteArray(selectedItem,0,selectedItem.size))
    }

    override fun getItemCount() = selectedList.size
}