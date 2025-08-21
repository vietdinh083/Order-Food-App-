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
import com.example.orderfood.model.OrderModel
import java.text.SimpleDateFormat

class RvHistoryAdapter(val listOrder: List<OrderModel>) :
    RecyclerView.Adapter<RvHistoryAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_rv_history,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val txtOrderId = findViewById<TextView>(R.id.txtOrderId)
            val  txtTime = findViewById<TextView>(R.id.txtTime)
            val imgFood = findViewById<ImageView>(R.id.imgFood)
            val  txtNameFood = findViewById<TextView>(R.id.txtNameFood)
            val txtPrice = findViewById<TextView>(R.id.txtPrice)
            val txtTotal = findViewById<TextView>(R.id.txtTotal)
            val txtQuantity = findViewById<TextView>(R.id.txtQuantity)

            txtOrderId.text = listOrder[position].id
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val current = formatter.format(listOrder[position].time)
            txtTime.text = current
            Glide.with(holder.itemView.context)
                .load(listOrder[position].cartList.get(0).image) // URL của hình ảnh
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
                .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
                .into(imgFood) // ImageView để hiển thị hình ảnh

            txtNameFood.text = listOrder[position].cartList[0].name
            txtPrice.text ="¥" + listOrder[position].cartList[0].price.toString()
            txtTotal.text = "¥" + listOrder[position].total.toString()
            txtQuantity.text =  listOrder[position].cartList[0].quantity.toString()
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}