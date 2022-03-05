package com.korilin.repository

import com.korilin.table.Question
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class QuestionRepository(database: Database) {
    private val questions = database.questions

    fun queryAllQuestions() = questions.toList()
}