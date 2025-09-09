package com.example.orderfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.database.AppDatabase
import com.example.orderfood.model.FavoriteModel


class RvFavoriteAdapter(private var listFavorite : MutableList<FavoriteModel> , private var OnClickListenter : RvItemClick) : RecyclerView.Adapter<RvFavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_rv_favorite,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.itemView.apply {
        val imgFood = findViewById<ImageView>(R.id.imgFood)
        val txtNameFood = findViewById<TextView>(R.id.txtNameFood)
        val txtPrice = findViewById<TextView>(R.id.txtPrice)
        val btnDelete = findViewById<ImageView>(R.id.btnDelete)

        Glide.with(holder.itemView.context)
            .load(listFavorite[position].image) // URL của hình ảnh
            .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
            .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
            .into(imgFood) // ImageView để hiển thị hình ảnh

        txtNameFood.text = listFavorite[position].name
        txtPrice.text ="¥" + listFavorite[position].price.toString()
        btnDelete.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
            builder
                .setTitle("Confirm")
                .setMessage("Do you want to delete ?")
                .setPositiveButton("Yes") { dialog, which ->
                    listFavorite[position].name?.let { it1 ->
                        AppDatabase.getInstance(context.applicationContext).favoriteDao().deleteFavorite(Utils.current_User.id,
                            it1
                        )
                        listFavorite.removeAt(position)

                        // Báo cho RecyclerView biết dữ liệu đã thay đổi
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listFavorite.size)
                        Toast.makeText(context.applicationContext, "Delete successfully", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
        holder.itemView.setOnClickListener {
            OnClickListenter.onItemClick(position)
        }
    }
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }
}