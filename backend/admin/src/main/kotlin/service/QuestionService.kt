package com.korilin.service

import com.korilin.bo.TestDataItem
import com.korilin.bo.testDataArrayType
import com.korilin.ktorm.globalJsonMapper
import com.korilin.model.Submits
import com.korilin.model.QuestionForm
import com.korilin.model.QuestionDetail
import com.korilin.domain.repository.InclusionRepository
import com.korilin.domain.repository.QuestionRepository
import com.korilin.domain.submitRecords
import com.korilin.domain.table.Question
import javaslang.Tuple2
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.forEach
import org.springframework.stereotype.Service

@Service
internal class QuestionService(
    private val questionRepository: QuestionRepository,
    private val inclusionRepository: InclusionRepository,
    database: Database
) {

    private val submitRecords = database.submitRecords

    internal suspend fun getAllQuestions() = questionRepository.queryAllQuestions()

    /**
     * 创建一个新问题，添加到数据库中，
     * 添加成功时返回新问题的 questionId
     */
    internal suspend fun newQuestion(questionForm: QuestionForm): Int? {
        val question = Question {
            type = questionForm.type!!
            level = questionForm.level!!
            title = questionForm.title!!
            description = questionForm.description!!
            codeTemplate = questionForm.codeTemplate!!
            testDataJson = globalJsonMapper.readValue(questionForm.testDataJson!!, testDataArrayType)
        }
        val int = questionRepository.newQuestion(question)
        return if (int == 1) {
            question.questionId
        } else null
    }

    internal suspend fun getQuestionDetail(questionId: Int): QuestionDetail? {
        return coroutineScope {
            val questionAsync = async { questionRepository.findQuestionById(questionId) }
            val contestsAsync = async { inclusionRepository.getContestsByQuestionId(questionId) }
            val submitsAsync = async {
                var count = 0
                var pass = 0
                submitRecords.filter { it.questionId eq questionId }.forEach {
                    count += 1
                    if (it.pass == 100) {
                        pass += 1
                    }
                }
                Submits(count, pass)
            }
            val question = questionAsync.await() ?: return@coroutineScope null
            val contests = contestsAsync.await()
            val submits = submitsAsync.await()
            QuestionDetail(question, contests.toTypedArray(), submits)
        }
    }

    /**
     * 更新指定问题 ID 的信息
     */
    internal suspend fun updateQuestion(questionId: Int, questionForm: QuestionForm): Tuple2<Boolean, String> {
        val title = questionForm.title
        val type = questionForm.type
        val level = questionForm.level
        val description = questionForm.description
        val codeTemplate = questionForm.codeTemplate
        val testDataJson: Array<TestDataItem>? = questionForm.testDataJson?.let {
            globalJsonMapper.readValue(it, testDataArrayType)
        }
        val result = questionRepository.updateQuestion(
            questionId = questionId,
            title = title,
            type = type,
            level = level,
            description = description,
            codeTemplate = codeTemplate,
            testDataJson = testDataJson
        )
        return Tuple2(result == 1, if (result == 1) "SUCCESS" else "更新记录 $result 条")
    }

    fun deleteQuestion(questionId: Int) = questionRepository.findQuestionById(questionId)?.let { question ->
        submitRecords.filter { it.questionId eq questionId }.forEach {
            it.delete()
        }
        question.delete() == 1
    }
}