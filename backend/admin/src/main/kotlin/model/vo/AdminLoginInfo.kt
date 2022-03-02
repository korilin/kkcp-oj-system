package com.korilin.model.vo

import java.time.LocalDateTime

data class AdminLoginInfo(
    val email: String,
    val period: LocalDateTime
)
