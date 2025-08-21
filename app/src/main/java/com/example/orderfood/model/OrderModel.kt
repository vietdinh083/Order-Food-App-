package com.example.orderfood.model

import android.icu.util.Calendar
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date


@IgnoreExtraProperties
data class OrderModel(
    val id: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val message: String = "",
    val payMethod: String = "",
    val total: Int = 0,
    val time: Date = Calendar.getInstance().time,
    val cartList: List<CartModel> = listOf()
){
    constructor():this("","","","","","", 0,Calendar.getInstance().time,mutableListOf())
}