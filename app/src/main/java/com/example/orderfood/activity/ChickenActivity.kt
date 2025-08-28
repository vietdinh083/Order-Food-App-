package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.adapter.RvFoodAdapter
import com.example.orderfood.model.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChickenActivity : AppCompatActivity() {
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mRecycleView: RecyclerView
    private lateinit var imgCart: ImageView
    private lateinit var toolbar: Toolbar
    //reuse RecommendFood as ChickenModel
    var mListOfChicken = mutableListOf<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chicken)
        inItId()
        getDataFromFirebase()
        ButtonActivity()
    }

    private fun ButtonActivity() {
        imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun inItId() {
        imgCart = findViewById(R.id.imgCart)
        mRecycleView = findViewById(R.id.recyclerView)
        mRecycleView.layoutManager = GridLayoutManager(this, 2)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getDataFromFirebase() {

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("food")

        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val mChicken = dataSnapshot.getValue(FoodModel::class.java)
                    if(mChicken != null && mChicken.type == "chicken"){
                        mListOfChicken.add(mChicken)
                    }

                    val adapter = RvFoodAdapter(mListOfChicken, object : RvItemClick {
                        override fun onItemClick(position: Int) {
                            val i: Intent = Intent(this@ChickenActivity, DetailActivity::class.java)
                            i.putExtra("name", mListOfChicken[position].name)
                            i.putExtra("description", mListOfChicken[position].description)
                            i.putExtra("price", mListOfChicken[position].price)
                            i.putExtra("image", mListOfChicken[position].image)
                            i.putExtra("type", mListOfChicken[position].type)
                            startActivity(i)
                        }

                    })
                    mRecycleView.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}