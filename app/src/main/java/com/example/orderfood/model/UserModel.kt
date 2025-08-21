package com.example.orderfood.model


class UserModel(
    var id: String,
    var email: String,
    var password: String,
) {
    constructor() : this("", "", "")
}