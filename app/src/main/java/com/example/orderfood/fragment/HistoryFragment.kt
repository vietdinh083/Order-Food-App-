package com.example.orderfood.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.adapter.RvHistoryAdapter
import com.example.orderfood.model.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(R.layout.fragment_history){
    private lateinit var recyclerViewHistory : RecyclerView
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private var mListOfOrder = mutableListOf<OrderModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("order").child(Utils.current_User.id)

        mDatabaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val mOrder = dataSnapshot.getValue(OrderModel::class.java)
                    if (mOrder != null) {
                        mListOfOrder.add(mOrder)
                    }
                }

                val adapter = RvHistoryAdapter(mListOfOrder)
                recyclerViewHistory.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}