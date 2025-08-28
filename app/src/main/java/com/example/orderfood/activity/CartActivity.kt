package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.adapter.OnCartChangeListener
import com.example.orderfood.adapter.RvCartAdapter
import com.example.orderfood.model.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnGoToOrder: Button
    private lateinit var adapter: RvCartAdapter
    private lateinit var toolbar: Toolbar


    // firebase database
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        inItId()
        setAdapterRecyclerViewCart()
        saveCartOnFirebase()
        getCartDatabaseFromFirebaseWhenUserOpenApp()
        btnOrderEventClick()

    }

    private fun btnOrderEventClick() {

            btnGoToOrder.setOnClickListener {
                if(Utils.listCart.isEmpty()){
                    Toast.makeText(this@CartActivity,"Your cart is empty",Toast.LENGTH_SHORT).show()
                }
                else{
                    val i = Intent(this, OrderActivity::class.java)
                    startActivity(i)
                }

            }

    }

    private fun getCartDatabaseFromFirebaseWhenUserOpenApp() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("cart").child(Utils.current_User.id)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (Utils.listCart.isEmpty()) {
                    for (dataSnapshot in snapshot.children) {
                        val cartModel = dataSnapshot.getValue(CartModel::class.java)
                        if (cartModel != null) {
                            Utils.listCart.add(cartModel)
                        }
                    }
                    setAdapterRecyclerViewCart()
                    totalPrice()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun saveCartOnFirebase() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("cart")
        if (Utils.listCart.isNotEmpty()) {
            for (item in Utils.listCart) {
                Utils.current_User.id.let {
                    mDatabaseReference
                        .child(it)
                        .child(item.name)
                        .setValue(item)
                        .addOnSuccessListener {
                            Log.d("Cart", "Save ${item.name} success")
                        }
                        .addOnFailureListener {
                            Log.e("Cart", "Error when save: ${it.message}")
                        }
                }
            }

        }


    }



    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterRecyclerViewCart() {
        if (!::adapter.isInitialized) {
            adapter = RvCartAdapter(Utils.listCart, object : OnCartChangeListener {
                override fun onCartUpdated() {
                    totalPrice()
                }
            })
            recyclerViewCart.adapter = adapter
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun totalPrice() {
        var sum: Int = 0
        if (Utils.listCart.isNotEmpty()) {
            for (i in 0..<Utils.listCart.size) {
                sum += Utils.listCart[i].price * Utils.listCart[i].quantity
            }
        }
        txtTotal.text = "¥$sum"
    }

    @SuppressLint("RestrictedApi")
    private fun inItId() {
        recyclerViewCart = findViewById(R.id.recyclerViewCart)
        recyclerViewCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        txtTotal = findViewById(R.id.txtTotal)
        btnGoToOrder = findViewById(R.id.btnGoToOrder)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged() // cập nhật dữ liệu nếu có thay đổi
        totalPrice()
    }
}
