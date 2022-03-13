package com.korilin.repository

import com.korilin.table.Question
import com.korilin.table.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.forEach
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class QuestionRepository(database: Database) {
    private val questions = database.questions
    private val questionSource = database.questionsSource

    /**
     * 获取所有 Question，但仅获取基本的信息，包括：问题ID、标题、类型、难度
     * @return 问题实体列表
     */
    fun queryAllQuestions() = questionSource.select(
        Questions.questionId, Questions.title, Questions.type, Questions.level
    ).map { Questions.createEntity(it) }

    /**
     * 获取指定问题的所有信息
     * @param questionId 问题 ID
     */
    fun findQuestionById(questionId: Int) = questions.find {
        it.questionId eq questionId
    }

    /**
     * 添加一个新的问题
     * @param question 新增加的问题，不需要设置 questionId
     */
    fun newQuestion(question: Question): Int {
        val now = LocalDateTime.now()
        question.descriptionLastUpdateTime = now
        question.codeTemplateLastUpdateTime = now
        question.testDataJsonLastUpdateTime = now
        return questions.add(question)
    }
}