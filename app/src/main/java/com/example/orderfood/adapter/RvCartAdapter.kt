package com.example.orderfood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R

import com.example.orderfood.Utils.Utils
import com.example.orderfood.model.CartModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RvCartAdapter(
    private val listCart: MutableList<CartModel>,
    private val listener: OnCartChangeListener,
) :
    RecyclerView.Adapter<RvCartAdapter.ViewHolder>() {
    private lateinit var dailog: AlertDialog
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_recycler_view_cart, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val txtNameFood = findViewById<TextView>(R.id.txtNameFood)
            val txtPrice = findViewById<TextView>(R.id.txtPrice)
            val txtQuantity = findViewById<TextView>(R.id.txtQuantity)
            val imgFood = findViewById<ImageView>(R.id.imgFood)
            val txtMinus = findViewById<TextView>(R.id.txtMinus)
            val txtPlus = findViewById<TextView>(R.id.txtPlus)
            //database
            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase.getReference("cart")


            // txtPlus event + update firebase-> cart -> quantity
            txtPlus.setOnClickListener {
                    listCart[position].quantity++
                    txtQuantity.text = listCart[position].quantity.toString()
                    Utils.current_User.id.let { it1 ->
                        mDatabaseReference
                            .child(it1)
                            .child(listCart[position].name)
                            .updateChildren(mapOf("quantity" to listCart[position].quantity))
                            .addOnSuccessListener {
                                notifyItemChanged(position)
                                listener.onCartUpdated()
                            }

                }


            }
            // txtMinus event + update firebase-> cart -> quantity
            txtMinus.setOnClickListener {
                if (listCart[position].quantity > 1) {
                    listCart[position].quantity--
                    txtQuantity.text = listCart[position].quantity.toString()
                    Utils.current_User.id.let { it1 ->
                        mDatabaseReference
                            .child(it1)
                            .child(listCart[position].name)
                            .updateChildren(mapOf("quantity" to listCart[position].quantity))
                            .addOnSuccessListener {
                                notifyItemChanged(position)
                                listener.onCartUpdated()
                            }
                    }

                } else if (listCart[position].quantity == 1) {

                    Utils.current_User.id.let { it1 ->
                        mDatabaseReference
                            .child(it1)
                            .child(listCart[position].name)
                            .removeValue()
                            .addOnSuccessListener {

                                val build = AlertDialog.Builder(this.context)
                                val view = LayoutInflater.from(this.context).inflate(R.layout.custom_alert_dailog, null)
                                val txtConfirmText = view.findViewById<TextView>(R.id.txtConfirmText)
                                txtConfirmText.setText("Do you want to remove this food in your cart?")
                                val imgCancel = view.findViewById<ImageView>(R.id.imgCancel)
                                val btnYes = view.findViewById<Button>(R.id.btnYes)
                                val btnNo = view.findViewById<Button>(R.id.btnNo)
                                imgCancel.setOnClickListener {
                                    dailog.dismiss()
                                }
                                btnNo.setOnClickListener {
                                    dailog.dismiss()
                                }
                                btnYes.setOnClickListener {
                                    listCart.removeAt(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, listCart.size)
                                    listener.onCartUpdated()
                                    dailog.dismiss()
                                }
                                build.setView(view)
                                dailog = build.create()
                                dailog.show()



                            }
                    }
                }


            }




            txtNameFood.text = listCart[position].name
            txtPrice.text = "¥" + listCart[position].price.toString()
            txtQuantity.text = listCart[position].quantity.toString()
            Glide.with(holder.itemView.context)
                .load(listCart[position].image) // URL của hình ảnh
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
                .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
                .into(imgFood) // ImageView để hiển thị hình ảnh

        }
    }

    override fun getItemCount(): Int {
        return listCart.size
    }
}

