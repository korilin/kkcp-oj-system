package com.korilin.model


data class LoginRequestBody(val email: String, val code: String)

data class NewQuestionForm(
    val type: Int,
    val level: Int,
    val title: String,
    val description: String, // 65,535 bytes
    val codeTemplate: String,
    val testJsonData: String // key 与 String 类型 value 必须使用双引号
)