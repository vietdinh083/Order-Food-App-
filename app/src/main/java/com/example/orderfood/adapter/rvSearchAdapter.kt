package com.example.orderfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.model.FoodModel

class rvSearchAdapter(private val listSearch: MutableList<FoodModel>, val OnItemClickListenner: RvItemClick) : RecyclerView.Adapter<rvSearchAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_rv_search, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val txtNameFood = findViewById<TextView>(R.id.txtNameFood)
            val txtPrice = findViewById<TextView>(R.id.txtPrice)
            val imgFood = findViewById<ImageView>(R.id.imgFood)

            txtNameFood.text = listSearch[position].name
            txtPrice.text = "¥"+listSearch[position].price.toString()
            Glide.with(holder.itemView.context)
                .load(listSearch[position].image) // URL của hình ảnh
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
                .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
                .into(imgFood) // ImageView để hiển thị hình ảnh

        }
        holder.itemView.setOnClickListener {
            OnItemClickListenner.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return listSearch.size
    }

}