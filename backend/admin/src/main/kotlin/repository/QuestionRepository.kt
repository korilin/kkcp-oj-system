package com.korilin.repository

import com.korilin.model.table.Question
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository

@Repository
class QuestionRepository(database: Database) {

    private val questions = database.questions

    /**
     * 获取所有问题
     * @return 问题列表 [Question]
     */
    internal fun queryAllQuestions() = questions.toList()

    /**
     * 获取指定 ID 的问题实体对象
     */
    internal fun queryQuestionById(questionId: Int) = questions.find {
        it.questionId eq questionId
    }
}