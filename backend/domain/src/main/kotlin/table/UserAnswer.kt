package com.korilin.domain.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

interface UserAnswer : Entity<UserAnswer> {

    companion object : Entity.Factory<UserAnswer>()

    var question: Question
    var attachId: Int
    var answer: String
}

object UserAnswers : Table<UserAnswer>("rt_user_answer") {
    val questionId = int("question_id").primaryKey().references(Questions) { it.question }
    val attachId = int("attach_id").primaryKey().bindTo { it.attachId }
    val answer = text("answer").bindTo { it.answer }
}
