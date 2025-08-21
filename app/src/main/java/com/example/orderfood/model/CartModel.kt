package com.example.orderfood.model

class CartModel(
    val name :String,
    val image:String,
    val price : Int,
    var quantity : Int
) {
    constructor():this("","",0,0)
}