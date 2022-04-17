package com.korilin.model

data class RegisterBody(
    val contestId: Int,
    val userId: Int
)

data class QuestionAnswer(
    val questionId: Int, val answer: String
)

data class AnswerUpdateBody(
    val userId: Int, val answers: Array<QuestionAnswer>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnswerUpdateBody

        if (userId != other.userId) return false
        if (!answers.contentEquals(other.answers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + answers.contentHashCode()
        return result
    }
}
