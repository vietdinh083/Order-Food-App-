package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfood.model.Photo
import com.example.orderfood.R
import com.example.orderfood.adapter.PhotoViewPager2Adapter
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.adapter.rvRecommendFoodAdapter
import com.example.orderfood.fragment.ChatFragment
import com.example.orderfood.fragment.MyPageFragment
import com.example.orderfood.fragment.HistoryFragment
import com.example.orderfood.fragment.HomeFragment
import com.example.orderfood.fragment.SearchFragment
import com.example.orderfood.model.FoodModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.relex.circleindicator.CircleIndicator3

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    //in it Id
    private lateinit var  imgButtonPizza : ImageButton
    private lateinit var  imgButtonRice : ImageButton
    private lateinit var  imgButtonChicken : ImageButton
    private lateinit var  imgButtonNoodle : ImageButton
    private lateinit var  imgButtonDrink : ImageButton
    private lateinit var  imgButtonMore : ImageButton
    private lateinit var frameLayoutContainer: FrameLayout

    private lateinit var imgCart:ImageView
    private lateinit var mViewPager2 : ViewPager2
    private lateinit var mCircleIndicator3 : CircleIndicator3
    //bottom Navigation
    private lateinit var mBottomNav : BottomNavigationView
    lateinit var adapter : PhotoViewPager2Adapter
    private var mListOfPhoto = mutableListOf<Photo>()

    //recycle view
    private lateinit var mRecyclerView : RecyclerView

    //firebase
    private lateinit var mDatabase : FirebaseDatabase
    private lateinit var mDatabaseReference : DatabaseReference
    var mListOfFoodModel = mutableListOf<FoodModel>()



    private val  mHandler:Handler = Handler()
    private val mRunnable = Runnable {
        if (mViewPager2.currentItem == mListOfPhoto.size - 1){
            mViewPager2.currentItem = 0
        }
        mViewPager2.currentItem += 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
//        toolbar.setNavigationIcon(R.drawable.ic_logo_nav)
        inItId()
        viewPageActivity()
        bottomNavigationActivity()
        getDataFromFirebase()
        ButtonActivity()



    }

    private fun ButtonActivity() {
        imgButtonPizza.setOnClickListener {
            val intent = Intent(this, PizzaActivity::class.java)
            startActivity(intent)
        }
        imgButtonRice.setOnClickListener {
            val intent = Intent(this, RiceActivity::class.java)
            startActivity(intent)
        }
        imgButtonChicken.setOnClickListener {
            val intent = Intent(this, ChickenActivity::class.java)
            startActivity(intent)
        }
        imgButtonNoodle.setOnClickListener {
            val intent = Intent(this, NoodleActivity::class.java)
            startActivity(intent)
        }
        imgButtonDrink.setOnClickListener {
            val intent = Intent(this, DrinkActivity::class.java)
            startActivity(intent)
        }
        imgButtonMore.setOnClickListener {
            val intent = Intent(this, MoreActivity::class.java)
            startActivity(intent)
        }
        imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }





    }

    private fun getDataFromFirebase() {
      mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("food")

        mDatabaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children){
                    val foodModel = dataSnapshot.getValue(FoodModel::class.java)
                    if(foodModel != null && foodModel.type == "recommend"){
                    mListOfFoodModel.add(foodModel)
                    }
                    val adapter = rvRecommendFoodAdapter(mListOfFoodModel, object :
                        RvItemClick {
                        override fun onItemClick(position: Int) {
                            val i: Intent = Intent(this@MainActivity, DetailActivity::class.java)
                            i.putExtra("name", mListOfFoodModel[position].name)
                            i.putExtra("description", mListOfFoodModel[position].description)
                            i.putExtra("price", mListOfFoodModel[position].price)
                            i.putExtra("image", mListOfFoodModel[position].image)
                            i.putExtra("type", mListOfFoodModel[position].type)
                            startActivity(i)
                        }


                    } )
                    mRecyclerView.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun bottomNavigationActivity() {
        mBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.nav_chat -> {
                    loadFragment(ChatFragment())
                    true
                }
                R.id.nav_contact -> {
                    loadFragment(MyPageFragment())
                    true
                }
                R.id.nav_history -> {
                    loadFragment(HistoryFragment())
                    true
                }

                else -> {
                    true
                }
            }
        }





    }

private  fun loadFragment(fragment: Fragment){
    frameLayoutContainer.visibility = View.VISIBLE

    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container,fragment)
    transaction.commit()
}


    private fun inItId(){
        mViewPager2 = findViewById(R.id.view_pager_2)
        mCircleIndicator3 = findViewById(R.id.circle_indicator_3)
        mBottomNav = findViewById(R.id.bottomNav)
        mRecyclerView = findViewById(R.id.rvRecommendFood)
        imgCart = findViewById(R.id.imgCart)
        //mRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        mRecyclerView.layoutManager = GridLayoutManager(this,2)
        imgButtonPizza = findViewById(R.id.imgButtonPizza)
        imgButtonRice = findViewById(R.id.imgButtonRice)
        imgButtonChicken = findViewById(R.id.imgButtonChicken)
        imgButtonNoodle = findViewById(R.id.imgButtonNoodle)
        imgButtonDrink = findViewById(R.id.imgButtonDrink)
        imgButtonMore = findViewById(R.id.imgButtonMore)
        frameLayoutContainer = findViewById(R.id.container)


    }

    //viewpae fun
    private fun viewPageActivity(){
        mListOfPhoto = getListOfPhoto()
        adapter = PhotoViewPager2Adapter(mListOfPhoto)
        mViewPager2.adapter = adapter
        mCircleIndicator3.setViewPager(mViewPager2)
        mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mHandler.removeCallbacks(mRunnable)
                mHandler.postDelayed(mRunnable,3000)
            }
        })

        mViewPager2.setPageTransformer(ZoomOutPageTransformer())

    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(mRunnable,3000)
    }

    private fun getListOfPhoto(): MutableList<Photo>{
        val listPhoto = mutableListOf<Photo>()
        listPhoto.add(Photo(R.drawable.xoiga))
        listPhoto.add(Photo(R.drawable.xoivo))
        listPhoto.add(Photo(R.drawable.comga))

        return listPhoto

    }
}