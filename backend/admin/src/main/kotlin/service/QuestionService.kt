package com.korilin.service

import com.korilin.bo.testDataArrayType
import com.korilin.ktorm.globalJsonMapper
import com.korilin.model.Commits
import com.korilin.model.NewQuestionForm
import com.korilin.model.QuestionDetail
import com.korilin.repository.QuestionRepository
import com.korilin.table.Question
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Service
internal class QuestionService(private val questionRepository: QuestionRepository) {

    internal suspend fun getAllQuestions() = questionRepository.queryAllQuestions()

    /**
     * 创建一个新问题，添加到数据库中，
     * 添加成功时返回新问题的 questionId
     */
    internal suspend fun newQuestion(questionForm: NewQuestionForm): Int? {
        val question = Question {
            type = questionForm.type
            level = questionForm.level
            title = questionForm.title
            description = questionForm.description
            codeTemplate = questionForm.codeTemplate
            testDataJson = globalJsonMapper.readValue(questionForm.testDataJson, testDataArrayType)
        }
        val int = questionRepository.newQuestion(question)
        return if (int == 1) {
            question.questionId
        } else {
            null
        }
    }

    internal suspend fun getQuestionDetail(questionId: Int): QuestionDetail? {
        return coroutineScope {
            val questionAsync = async { questionRepository.queryQuestionById(questionId) }
            val contestsAsync = async { listOf<Unit>() }
            val commitsAsync = async { Commits(100, 45) }
            val question = questionAsync.await() ?: return@coroutineScope null
            val contests = contestsAsync.await()
            val commits = commitsAsync.await()
            QuestionDetail(question, contests.toTypedArray(), commits)
        }
    }
}