package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.model.MessageModel

class RvMessageAdapter ( var messageList : MutableList<MessageModel>): RecyclerView.Adapter<RvMessageAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RvMessageAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_rv_message, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RvMessageAdapter.ViewHolder, position: Int) {

            holder.itemView.apply {
                val message = messageList[position]
                val txtUserMessage = findViewById<TextView>(R.id.txtUserMessage)
                val txtBotMessage = findViewById<TextView>(R.id.txtBotMessage)

                if (message.role == "user") {
                    txtBotMessage.visibility = View.GONE
                    txtUserMessage.visibility = View.VISIBLE
                    txtUserMessage.text = message.message

                }
                else{
                    txtUserMessage.visibility = View.GONE
                    txtBotMessage.visibility = View.VISIBLE
                    txtBotMessage.text = message.message
                }

            }
        }


        override fun getItemCount(): Int {
            return messageList.size
        }

    }
