package com.example.orderfood.Utils

import com.example.orderfood.model.CartModel
import com.example.orderfood.model.FavoriteModel
import com.example.orderfood.model.MessageModel
import com.example.orderfood.model.UserModel

object Utils {
    val listCart = mutableListOf<CartModel>()
    fun add(cartModel: CartModel) {
        listCart.add(cartModel)
    }
    var current_User = UserModel("","","")
    val messageList = mutableListOf<MessageModel>()


}