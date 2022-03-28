package com.korilin.repository

import com.korilin.bo.TestDataItem
import com.korilin.table.Question
import com.korilin.table.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
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

    /**
     * 获取指定问题 ID 的 Question 对象，对非 null 参数进行更新，
     * 如果存在关联更新时间的字段，则对数据库存储的更新时间进行更新
     * @return 更新的条目数
     */
    fun updateQuestion(
        questionId: Int, title: String?, type: Int?, level: Int?, description: String?, codeTemplate: String?,
        testDataJson:
        Array<TestDataItem>?
    ): Int {
        val question = findQuestionById(questionId) ?: return 0
        val now = LocalDateTime.now()
        title?.let { question.title = it }
        type?.let { question.type = it }
        level?.let { question.level = it }
        description?.let {
            question.description = it
            question.descriptionLastUpdateTime = now
        }
        codeTemplate?.let {
            question.codeTemplate = it
            question.codeTemplateLastUpdateTime = now
        }
        testDataJson?.let {
            question.testDataJson = it
            question.testDataJsonLastUpdateTime = now
        }
        return question.flushChanges()
    }
}