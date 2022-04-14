package com.korilin.model

import com.korilin.table.Contest
import com.korilin.table.Question
import com.korilin.table.UserProfile

data class ContestRecord(
    val contest: Contest,
    val questions: Array<Question>,
    val rank: Array<UserProfile>
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