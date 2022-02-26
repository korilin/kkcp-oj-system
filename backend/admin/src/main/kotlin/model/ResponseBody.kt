package com.korilin.model

import com.korilin.table.AdminAccount

/**
 * 登录响应数据模型
 * @param token 用户密钥 - 实质是用户信息转码后加密得到的标识
 * @param account 用户 ID
 */
data class LoginResponseBody(
    val token: String,
    var account: AdminAccount
)