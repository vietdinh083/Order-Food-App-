package com.example.orderfood.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.adapter.RvMessageAdapter
import com.example.testchatbot.ChatViewModel

class ChatFragment : Fragment(R.layout.fragment_chat){
    private  lateinit var rvMessage : RecyclerView
    private lateinit var edtMessage :EditText
    private lateinit var  btnSend : ImageButton
    val chatViewModel : ChatViewModel = ChatViewModel()
    private lateinit var adapter: RvMessageAdapter
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inItId(view)
        setRecycleViewAdapter()

        btnSend.setOnClickListener {
            adapter.notifyDataSetChanged()
            val message = edtMessage.text.toString()
            if (message.isEmpty()){
                return@setOnClickListener
            }else{
            chatViewModel.sendQuestion(message) {
                adapter.notifyDataSetChanged()
                rvMessage.scrollToPosition(chatViewModel.messageList.size - 1)
            }
            }
            edtMessage.setText("")
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecycleViewAdapter() {
        adapter = RvMessageAdapter(chatViewModel.messageList)
        adapter.notifyDataSetChanged()
        rvMessage.adapter = adapter
        rvMessage.layoutManager = LinearLayoutManager(this.context)
    }

    private fun inItId(view: View) {
        rvMessage = view.findViewById(R.id.rvMessage)
        edtMessage = view.findViewById(R.id.edtMessage)
        btnSend = view.findViewById(R.id.btnSend)
    }
}