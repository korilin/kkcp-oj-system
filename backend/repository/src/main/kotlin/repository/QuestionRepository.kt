package com.korilin.repository

import com.korilin.questions
import org.ktorm.database.Database
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository

@Repository
class QuestionRepository(database: Database) {
    private val questions = database.questions

    fun queryAllQuestions() = questions.toList()
}