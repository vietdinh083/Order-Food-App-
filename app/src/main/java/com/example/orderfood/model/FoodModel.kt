package com.example.orderfood.model

class FoodModel(
    val id: Int,
    val description: String,
    val image: String,
    val name: String,
    val price: Int,
    val type: String
) {
    constructor() : this(0, "", "", "", 0,"")
}