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

    /**
     * 添加一个新的问题
     * @param question 新增加的问题，不需要设置 questionId
     */
    fun newQuestion(question: Question): Int {
        val now = LocalDateTime.now()
        question.codeTemplateLastUpdateTime = now
        question.testJsonDataLastUpdateTime = now
        return questions.add(question)
    }
}