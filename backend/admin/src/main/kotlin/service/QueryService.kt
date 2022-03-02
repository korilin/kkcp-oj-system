package com.korilin.service

import com.korilin.model.table.Question
import com.korilin.model.vo.QuestionModel
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
internal class QueryService() {

    internal suspend fun getAllQuestions() = transaction {
        Question.all().map {
            QuestionModel.fromEntity(it)
        }
    }
}