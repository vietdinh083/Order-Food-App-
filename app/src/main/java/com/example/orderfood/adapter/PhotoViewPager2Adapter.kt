package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.model.Photo
import com.example.orderfood.R

class PhotoViewPager2Adapter (val mListPhoto: MutableList<Photo>):RecyclerView.Adapter<PhotoViewPager2Adapter.PhotoViewHolder>(){
    inner class PhotoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imgViewPager: ImageView = itemView.findViewById(R.id.imgViewPager)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_view_pager_2,parent,false)
        return PhotoViewHolder(view)

    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = mListPhoto[position]
        holder.imgViewPager.setImageResource(photo.resourceId)
    }

    override fun getItemCount(): Int {
        return mListPhoto.size
    }
}