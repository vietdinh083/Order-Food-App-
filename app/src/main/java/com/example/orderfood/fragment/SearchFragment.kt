package com.example.orderfood.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.activity.DetailActivity
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.adapter.rvSearchAdapter
import com.example.orderfood.model.FoodModel
import com.google.firebase.database.*
import java.util.Locale

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var edtSearch: EditText
    private lateinit var rvSearch: RecyclerView
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var searchAdapter: rvSearchAdapter
    private var mListOfSearch = mutableListOf<FoodModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        searchEvent()
    }

    private fun searchEvent() {

        searchAdapter = rvSearchAdapter(mListOfSearch,object : RvItemClick {
            override fun onItemClick(position: Int) {
                val i: Intent = Intent(requireContext(), DetailActivity::class.java)
                i.putExtra("name", mListOfSearch[position].name)
                i.putExtra("description", mListOfSearch[position].description)
                i.putExtra("price", mListOfSearch[position].price)
                i.putExtra("image", mListOfSearch[position].image)
                i.putExtra("type", mListOfSearch[position].type)
                startActivity(i)
            }
        })
        rvSearch.adapter = searchAdapter

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("food")

        edtSearch.addTextChangedListener(object : TextWatcher {
            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {
                val tmp = s.toString().trim()

                if (tmp.isNotEmpty()) {
                    val query = tmp[0].uppercase() + tmp.substring(1).lowercase(Locale.getDefault())
                    searchFirebase(query)
                } else {
                    mListOfSearch.clear()
                    searchAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun initView(view: View) {
        edtSearch = view.findViewById(R.id.edtSearch)
        rvSearch = view.findViewById(R.id.rvSearch)
        rvSearch.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun searchFirebase(query: String) {
        val searchQuery = mDatabaseReference
            .orderByChild("name")
            .startAt(query)
            .endAt(query + "\uf8ff") // chính xác: nối query + "\uf8ff"

        searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mListOfSearch.clear()
                for (child in snapshot.children) {
                    val food = child.getValue(FoodModel::class.java)
                    food?.let { mListOfSearch.add(it) }
                    
                }
                searchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }
}
