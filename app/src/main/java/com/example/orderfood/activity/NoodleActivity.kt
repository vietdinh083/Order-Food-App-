package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

class NoodleActivity : AppCompatActivity() {
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mRecycleView: RecyclerView
    private lateinit var imgCart: ImageView
    var mListOfNoodle = mutableListOf<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_noodle)
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

    private fun inItId() {
        imgCart = findViewById(R.id.imgCart)
        mRecycleView = findViewById(R.id.recyclerView)
        mRecycleView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun getDataFromFirebase() {

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("food")

        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val mNoodle = dataSnapshot.getValue(FoodModel::class.java)
                    if(mNoodle != null && mNoodle.type == "noodle"){
                        mListOfNoodle.add(mNoodle)
                    }

                    val adapter = RvFoodAdapter(mListOfNoodle, object : RvItemClick {
                        override fun onItemClick(position: Int) {
                            val i: Intent = Intent(this@NoodleActivity, DetailActivity::class.java)
                            i.putExtra("name", mListOfNoodle[position].name)
                            i.putExtra("description", mListOfNoodle[position].description)
                            i.putExtra("price", mListOfNoodle[position].price)
                            i.putExtra("image", mListOfNoodle[position].image)
                            i.putExtra("type", mListOfNoodle[position].type)
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