package com.example.testchatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderfood.Utils.Utils
import com.example.orderfood.model.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList = mutableListOf<MessageModel>()
   val  mDatabase = FirebaseDatabase.getInstance()
   val mDatabaseReference = mDatabase.getReference("food")
    init {
        // say Hello when user use chat bot
        if (Utils.messageList.isEmpty()) {
            Utils.messageList.add(MessageModel("Hello, how can I help you today?", "model"))
        }
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    private fun loadMenuData(onComplete: (String) -> Unit) {
        mDatabaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val builder = StringBuilder()
                for (child in snapshot.children) {
                    val name = child.child("name").getValue(String::class.java)
                    val price = child.child("price").getValue(Int::class.java)
                    if (name != null && price != null) {
                        builder.append("$name: $price ¥\n")
                    }
                }
                onComplete(builder.toString())
            } else {
                onComplete("Menu data is not available.")
            }
        }.addOnFailureListener {
            onComplete("Failed to load menu from Firebase.")
        }
    }

    fun sendQuestion(question: String, onResult: () -> Unit) {
        // Add user question
        Utils.messageList.add(MessageModel(question, "user"))
        Utils.messageList.add(MessageModel("Typing...", "model"))
        onResult()

        // Load data from Firebase before sending to Gemini
        loadMenuData { menuData ->
            viewModelScope.launch {
                try {
                    val chat = generativeModel.startChat(
                        history = Utils.messageList.map {
                            content(it.role) { text(it.message) }
                        }
                    )

                    // Combine Firebase data into the prompt
                    val prompt = """
This is the restaurant menu. 
You can provide recommendations to customers about the nutritional content of the dishes and suggest meals that match their dietary needs.
 You should also explain the ingredients of each dish when asked.
  However, you cannot place an order for the customer. If the customer asks you to order on their behalf, you must politely decline :
                    $menuData
                    
                    Customer's question: $question
                """.trimIndent()

                    val respond = chat.sendMessage(prompt)
                    // replace "*" && "**" to ""
                    val cleanText = respond.text.toString().replace("**", "").replace("*", "")

                    // Replace "Typing..." with real response
                    Utils.messageList.removeAt(Utils.messageList.size - 1)
                    Utils.messageList.add(MessageModel(cleanText, "model"))
                    onResult()

                } catch (e: Exception) {
                    Utils.messageList.removeAt(Utils.messageList.size - 1)
                    Utils.messageList.add(MessageModel("Error: ${e.message.toString()}", "model"))
                    onResult()

                }

            }
        }
    }
}
