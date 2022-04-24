package com.korilin.model

import java.time.LocalDateTime


data class LoginRequestBody(val email: String, val code: String)

data class QuestionForm(
    val type: Int?, val level: Int?, val title: String?, val description: String?, // 65,535 bytes
    val codeTemplate: String?, val testDataJson: String? // key 与 String 类型 value 必须使用双引号
)

data class ContestForm(
    val title: String?,
    val type: Int?,
    val description: String?,
    val duration: Int?,
    val startTime: LocalDateTime?,
)

/**
 * @param questions Question ID 列表
 */
data class InclusionRequest(
    val contestId: Int, val questions: Array<Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InclusionRequest

        if (contestId != other.contestId) return false
        if (!questions.contentEquals(other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contestId
        result = 31 * result + questions.contentHashCode()
        return result
    }
}

data class Admin(
    val email: String,
    val name: String,
    val level: Int
)
