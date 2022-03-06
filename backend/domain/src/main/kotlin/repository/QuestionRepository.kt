package com.korilin.repository

import com.korilin.table.Question
import com.korilin.table.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class QuestionRepository(database: Database) {
    private val questions = database.questions
    private val questionSource = database.questionsSource

    fun queryAllQuestions() = questionSource.select(
        Questions.questionId, Questions.title, Questions.type, Questions.level
    ).map { Questions.createEntity(it) }

    /**
     * 添加一个新的问题
     * @param question 新增加的问题，不需要设置 questionId
     */
    fun newQuestion(question: Question): Int {
        val now = LocalDateTime.now()
        question.codeTemplateLastUpdateTime = now
        question.testDataJsonLastUpdateTime = now
        return questions.add(question)
    }
}