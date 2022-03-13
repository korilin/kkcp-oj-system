package com.korilin.service

import com.korilin.bo.TestDataItem
import com.korilin.bo.testDataArrayType
import com.korilin.ktorm.globalJsonMapper
import com.korilin.model.Commits
import com.korilin.model.QuestionForm
import com.korilin.model.QuestionDetail
import com.korilin.repository.QuestionRepository
import com.korilin.table.Question
import javaslang.Tuple2
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
internal class QuestionService(private val questionRepository: QuestionRepository) {

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
            val contestsAsync = async { listOf<Unit>() }
            val commitsAsync = async { Commits(100, 45) }
            val question = questionAsync.await() ?: return@coroutineScope null
            val contests = contestsAsync.await()
            val commits = commitsAsync.await()
            QuestionDetail(question, contests.toTypedArray(), commits)
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
        val testDataJson: Array<TestDataItem> = globalJsonMapper.readValue(questionForm.testDataJson, testDataArrayType)
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
}