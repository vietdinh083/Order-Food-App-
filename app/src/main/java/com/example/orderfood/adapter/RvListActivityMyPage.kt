package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R

class RvListActivityMyPage(private var ListActivity: MutableList<String> , var OnItemClick : RvItemClick) : RecyclerView.Adapter<RvListActivityMyPage.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_rv_my_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val txtNameActivity = findViewById<TextView>(R.id.txtNameActivity)
            txtNameActivity.text = ListActivity[position]
        }
        holder.itemView.setOnClickListener {
            OnItemClick.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
       return ListActivity.size
    }

}