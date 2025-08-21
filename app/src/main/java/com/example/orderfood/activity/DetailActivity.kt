package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.adapter.rvRecommendFoodAdapter
import com.example.orderfood.model.CartModel
import com.example.orderfood.model.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {
    private lateinit var imgFood: ImageView
    private lateinit var txtNameFood: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDescription: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var btnAddToCart: Button
    private lateinit var txtQuantity: TextView
    private lateinit var txtPlus: TextView
    private lateinit var txtMinus: TextView
    private lateinit var imgCart: ImageView

    //alert dailog
    private lateinit var dailog: AlertDialog

    //firebase
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    var mListOfFoodModel = mutableListOf<FoodModel>()
    var mQuantity = 0

    var mImage: String? = null
    var mPrice: Int? = null
    var mName: String? = null
    var mDescription: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        inItId()
        getDataIntent()
        getDataFromFirebase()
        txtPlusAndMinusEventClick()
        btnAddToCartEventClick()
        btnCartEventClick()


    }

    private fun btnCartEventClick() {
        val i = Intent(this, CartActivity::class.java)
        imgCart.setOnClickListener {
            startActivity(i)
        }
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun btnAddToCartEventClick() {

        btnAddToCart.setOnClickListener {
            if (mQuantity != 0) {
                val build = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.custom_alert_dailog, null)
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
                    val cartModel = CartModel(
                        mName.toString(),
                        mImage.toString(), mPrice!!.toInt(), mQuantity
                    )
                    // add to list cart
                    addToCart(cartModel)
                    saveCartOnFirebase()
                    dailog.dismiss()
                    Toast.makeText(this, "Add to cart successfully", Toast.LENGTH_SHORT).show()
                    txtQuantity.text = "0"
                }
                build.setView(view)
                dailog = build.create()
                dailog.show()

            } else {
                Toast.makeText(this, "Please choose quantity", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun addToCart(newItem: CartModel) {
        var isItemExist = false

        for (item in Utils.listCart) {
            if (item.name == newItem.name) {
                item.quantity += newItem.quantity
                isItemExist = true
                break
            }
        }

        if (!isItemExist) {
            Utils.listCart.add(newItem)
        }

    }

    private fun txtPlusAndMinusEventClick() {
        txtPlus.setOnClickListener {
            mQuantity++
            txtQuantity.text = mQuantity.toString()
        }
        txtMinus.setOnClickListener {
            if (mQuantity > 0) {
                mQuantity--
                txtQuantity.text = mQuantity.toString()
            } else {
                mQuantity = 0
                txtQuantity.text = mQuantity.toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDataIntent() {
        val i = intent
        mName = i.getStringExtra("name").toString()
        mPrice = i.getIntExtra("price", 0)
        mImage = i.getStringExtra("image").toString()
        mDescription = i.getStringExtra("description").toString()
        //set View
        txtNameFood.text = mName
        txtDescription.text = mDescription
        txtPrice.text = "¥" + mPrice.toString()
        //init

        Glide.with(applicationContext).load(mImage)
            .placeholder(R.drawable.ic_launcher_background) // Hình ảnh tạm thời khi đang tải
            .error(R.drawable.ic_launcher_foreground) // Hình ảnh lỗi khi tải thất bại
            .into(imgFood)
    }

    private fun getDataFromFirebase() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("food")

        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val foodModel = dataSnapshot.getValue(FoodModel::class.java)
                    if(foodModel != null && foodModel.type == "recommend"){
                        mListOfFoodModel.add(foodModel)
                    }
                    val adapter = rvRecommendFoodAdapter(mListOfFoodModel, object :
                        RvItemClick {
                        override fun onItemClick(position: Int) {
                            val i = Intent(this@DetailActivity, DetailActivity::class.java)
                            i.putExtra("name", mListOfFoodModel[position].name)
                            i.putExtra("description", mListOfFoodModel[position].description)
                            i.putExtra("price", mListOfFoodModel[position].price)
                            i.putExtra("image", mListOfFoodModel[position].image)
                            i.putExtra("type", mListOfFoodModel[position].type)
                            startActivity(i)
                        }

                    })
                    mRecyclerView.adapter = adapter

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

    private fun inItId() {
        imgFood = findViewById(R.id.imgFood)
        txtNameFood = findViewById(R.id.txtNameFood)
        txtPrice = findViewById(R.id.txtPrice)
        txtDescription = findViewById(R.id.txtDescription)
        mRecyclerView = findViewById(R.id.recyclerView)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        txtQuantity = findViewById(R.id.txtQuantity)
        imgCart = findViewById(R.id.imgCart)
        txtPlus = findViewById(R.id.txtPlus)
        txtMinus = findViewById(R.id.txtMinus)
        mRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}