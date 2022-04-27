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

data class User(
    var id: Int,
    var login: String,
    var name: String,
    var email: String,
    var avatarUrl: String,
    var bio: String,
    var htmlUrl: String,
    var block: Boolean
)