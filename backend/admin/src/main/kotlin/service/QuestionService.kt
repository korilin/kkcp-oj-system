package com.korilin.service

import com.korilin.bo.TestDataItem
import com.korilin.bo.testDataArrayType
import com.korilin.ktorm.globalJsonMapper
import com.korilin.model.NewQuestionForm
import com.korilin.repository.QuestionRepository
import com.korilin.table.Question
import org.springframework.stereotype.Service

@Service
internal class QuestionService(private val questionRepository: QuestionRepository) {

    internal suspend fun getAllQuestions() = questionRepository.queryAllQuestions()

    /**
     * 创建一个新问题，添加到数据库中，
     * 添加成功时返回新问题的 questionId
     */
    internal suspend fun newQuestion(questionForm: NewQuestionForm) : Int? {
        val question = Question {
            type = questionForm.type
            level = questionForm.level
            title = questionForm.title
            description = questionForm.description
            codeTemplate = questionForm.codeTemplate
            testJsonData =globalJsonMapper.readValue(questionForm.testJsonData, testDataArrayType)
        }
        val int = questionRepository.newQuestion(question)
        return if (int == 1) {
             question.questionId
        } else {
            null
        }
    }
}