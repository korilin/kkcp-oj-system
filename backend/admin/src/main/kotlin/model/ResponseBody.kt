package com.korilin.model

import com.korilin.domain.table.*
import java.time.LocalDateTime

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
    val contests: Array<Contest>,
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

data class ContestRegistration(
    val profile: UserProfile,
    val time: LocalDateTime,
    val answers: Array<SubmitRecord>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContestRegistration

        if (profile != other.profile) return false
        if (time != other.time) return false
        if (!answers.contentEquals(other.answers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = profile.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + answers.contentHashCode()
        return result
    }
}