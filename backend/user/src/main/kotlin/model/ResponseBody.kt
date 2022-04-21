package com.korilin.model

import com.korilin.domain.table.*

data class QuestionAndAnswer(val question: Question, val answer: String)
/**
 * 活动比赛数据，Question 包含的是详细数据
 */
data class UnderWayContestUserData(
    val contest: Contest,
    val questions: Array<QuestionAndAnswer>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnderWayContestUserData

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

/**
 * 活动记录数据，Question 包含的是基本数据
 */
data class ContestRecord(
    val contest: Contest,
    val questions: Array<Question>,
    val rank: Array<RankInfo>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContestRecord

        if (contest != other.contest) return false
        if (!questions.contentEquals(other.questions)) return false
        if (!rank.contentEquals(other.rank)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contest.hashCode()
        result = 31 * result + questions.contentHashCode()
        result = 31 * result + rank.contentHashCode()
        return result
    }
}

data class RankInfo(
    val user: UserProfile,
    val answer: Array<SubmitRecord?>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RankInfo

        if (user != other.user) return false
        if (!answer.contentEquals(other.answer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }
}