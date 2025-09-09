package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.adapter.RvFavoriteAdapter
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.database.AppDatabase
import com.example.orderfood.model.FavoriteModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var rvFavorite: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        inItId()
        setRecycleView()

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setRecycleView() {
        val mFavoriteList: MutableList<FavoriteModel> =
            AppDatabase.getInstance(this).favoriteDao().getFavoriteList(Utils.current_User.id)
        val adapter = RvFavoriteAdapter(mFavoriteList , object : RvItemClick {
            override fun onItemClick(position: Int) {
                val i: Intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                i.putExtra("name", mFavoriteList[position].name)
                i.putExtra("description", mFavoriteList[position].description)
                i.putExtra("price", mFavoriteList[position].price)
                i.putExtra("image", mFavoriteList[position].image)
                startActivity(i)
            }

        })
        adapter.notifyDataSetChanged()
        rvFavorite.adapter = adapter
    }

    @SuppressLint("RestrictedApi")
    private fun inItId() {
        rvFavorite = findViewById(R.id.rvFavorite)
        rvFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}