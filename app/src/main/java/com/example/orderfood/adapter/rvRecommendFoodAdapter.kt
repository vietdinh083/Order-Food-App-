package com.example.orderfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView

import com.example.orderfood.R
import com.example.orderfood.model.FoodModel

class rvRecommendFoodAdapter(private val foodModelList: List<FoodModel>, val OnItemClickListenner: RvItemClick) :
    RecyclerView.Adapter<rvRecommendFoodAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_recycle_view_recommend, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val txtNameFood = findViewById<TextView>(R.id.txtNameFood)
            val txtPrice = findViewById<TextView>(R.id.txtPrice)
            val imgFood = findViewById<ImageView>(R.id.imgFood)
            val imgFavorite = findViewById<ImageView>(R.id.imgFavorite)

            txtNameFood.text = foodModelList[position].name
            txtPrice.text = "¥"+foodModelList[position].price.toString()
            Glide.with(holder.itemView.context)
                .load(foodModelList[position].image) // URL của hình ảnh
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
                .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
                .into(imgFood) // ImageView để hiển thị hình ảnh

            var isFavorite = false

            imgFavorite.setOnClickListener {
                if (isFavorite) {
                    imgFavorite.setImageResource(R.drawable.ic_favorite_border)

                } else {
                    imgFavorite.setImageResource(R.drawable.ic_favorite_full)

                }
                isFavorite = !isFavorite
            }
        }
        holder.itemView.setOnClickListener {
            OnItemClickListenner.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return foodModelList.size
    }
}