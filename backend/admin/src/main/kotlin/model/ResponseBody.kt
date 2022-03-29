package com.korilin.model

import com.korilin.table.AdminAccount
import com.korilin.table.Contest
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
    val contest: Array<Unit>,
    val commits: Commits
)

data class Commits(
    val commitCount: Int,
    val passCount: Int,
)

data class ContestInfo(
    val contest: Contest,
    val questions: Array<Question>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContestInfo

        if (contest != other.contest) return false
        if (!questions.contentEquals(other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contest.hashCode()
        result = 31 * result + questions.contentHashCode()
        return result
    }
}