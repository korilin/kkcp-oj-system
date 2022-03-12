package com.korilin.model

import com.korilin.table.AdminAccount
import com.korilin.table.Question

/**
 * 登录响应数据模型
 * @param token 用户密钥 - 实质是用户信息转码后加密得到的标识
 * @param account 用户 ID
 */
data class AdminLoginModel(
    val token: String,
    var account: AdminAccount
)

class QuestionDetail(
    val question: Question,
    val inclusionContests: Array<Unit>,
    val commits: Commits
)

data class Commits(
    val commitCount: Int,
    val passCount: Int,
)
