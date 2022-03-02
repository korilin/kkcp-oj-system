package com.korilin.model.vo

import java.time.LocalDateTime

data class AdminLoginInfo(
    val email: String,
    val period: LocalDateTime
)

/**
 * 登录响应数据模型
 * @param token 用户密钥 - 实质是用户信息转码后加密得到的标识
 * @param account 用户 ID
 */
data class AdminLoginModel(
    val token: String,
    var account: AdminLoginInfo
)