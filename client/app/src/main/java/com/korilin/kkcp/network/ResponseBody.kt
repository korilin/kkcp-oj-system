package com.korilin.kkcp.network


data class ResponseBody<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)

data class LoginResponse(
    val token: String,
    var account: Account
)

data class Account(
    var email: String,
    var name: String,
    var level: Int,
    var lastLoginTime: String
)