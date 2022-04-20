package com.korilin.service

import com.korilin.bo.ContestStatus
import com.korilin.bo.TestDataItem
import com.korilin.model.QuestionAndAnswer
import com.korilin.model.QuestionAnswer
import com.korilin.repository.*
import com.korilin.table.Registration
import com.korilin.table.UserAnswer
import com.korilin.utils.CodeUtil
import com.korilin.utils.AnswerClassHelper
import com.korilin.utils.AnswerVerifyHelper
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

@Service
class MainService(
    private val registrationRepository: RegistrationRepository,
    private val contestRepository: ContestRepository,
    private val questionRepository: QuestionRepository,
    private val inclusionRepository: InclusionRepository,
    database: Database
) {

    private val userProfiles = database.userProfiles
    private val userAnswers = database.userAnswers

    suspend fun register(contestId: Int, userId: Int): Pair<Boolean, String> {
        if (registrationRepository.getRegistration(contestId, userId) != null) {
            return Pair(false, "已报名过了")
        }
        val contest = contestRepository.findContestById(contestId) ?: throw NullPointerException("找不到对应活动")
        if (contest.status != ContestStatus.RELEASE.id) return Pair(false, "活动不处于报名期")
        val user = userProfiles.find { it.id eq userId } ?: throw NullPointerException("找不到对应用户")
        val result = registrationRepository.register(contest, user)
        return Pair(result, "原因不明")
    }

    /**
     * 获取用户报名了并且正在进行中的活动注册信息
     */
    suspend fun getUserRegisterUnderWayContest(userId: Int): Pair<String, Registration?> {
        val contest = contestRepository.findMainTargetContest()
        if (contest?.status != ContestStatus.UNDERWAY.id) return Pair("没有进行中的活动", null)
        val registration = registrationRepository.getRegistration(contest.contestId, userId)
            ?: return Pair("你没有报名该比赛，无法参加答题", null)
        return Pair("", registration)
    }

    suspend fun getQuestionsWithAnswer(registration: Registration): Array<QuestionAndAnswer> {
        val questions = inclusionRepository.getQuestionsDetailByContestId(registration.contest.contestId)
        val answers = questions.map { question ->
            // 过滤问题代码模版和测试数据
            question.codeTemplate = CodeUtil.getUserCodeTemplate(question.codeTemplate)
            question.testDataJson = question.testDataJson.slice(0..2).toTypedArray()
            val qId = question.questionId
            val attachId = registration.attachId
            var userAnswer = userAnswers.find { (it.questionId eq qId) and (it.attachId eq attachId) }
            if (userAnswer == null) {
                userAnswer = UserAnswer {
                    this.question = question
                    this.answer = question.codeTemplate
                    this.attachId = registration.attachId
                }
                userAnswers.add(userAnswer)
            }
            QuestionAndAnswer(question, userAnswer.answer)
        }
        return answers.toTypedArray()
    }

    suspend fun updateAnswer(userId: Int, answers: Array<QuestionAnswer>): Boolean {
        val contest = contestRepository.findMainTargetContest() ?: return false
        val registration = registrationRepository.getRegistration(contest.contestId, userId) ?: return false
        val attachId = registration.attachId
        val hashMap = HashMap<Int, UserAnswer>()
        userAnswers.filter { it.attachId eq attachId }.forEach {
            hashMap[it.question.questionId] = it
        }
        for (answer in answers) {
            hashMap[answer.questionId]?.let {
                it.answer = answer.answer
                it.flushChanges()
            }
        }
        registration.answerLastUpdateTime = LocalDateTime.now()
        return true
    }

    suspend fun pretreatment(
        userId: Int,
        questionId: Int,
        answer: String,
        block: suspend (Class<*>, Array<TestDataItem>) -> Pair<Boolean, String>
    ): Pair<Boolean, String> {
        val contest = contestRepository.findMainTargetContest()
        if (contest == null || contest.status != ContestStatus.UNDERWAY.id) return Pair(false, "找不到进行中的竞赛")
        registrationRepository.getRegistration(contest.contestId, userId) ?: return Pair(false, "没有报名，无法提交答案")
        val questions = inclusionRepository.getQuestionsDetailByContestId(contest.contestId)
        val question = questions.find { it.questionId == questionId } ?: return Pair(false, "该竞赛没有对应题目")
        val code = CodeUtil.compositeAnswerCode(question.codeTemplate, answer)
        val clazz = AnswerClassHelper.createClass(questionId, userId, code)
        return block(clazz, question.testDataJson)
    }

    suspend fun testAnswer(userId: Int, questionId: Int, answer: String): Pair<Boolean, String> {
        return pretreatment(userId, questionId, answer) { clazz, data ->
            val testData = data.map {
                it.toMap()
            }.slice(0..2).toTypedArray()
            AnswerVerifyHelper.verifyAnswer(clazz, testData)
        }
    }

    suspend fun submitAnswer(userId: Int, questionId: Int, answer: String): Pair<Boolean, String> {
        return pretreatment(userId, questionId, answer) { clazz, data ->
            val testData = data.map {
                it.toMap()
            }.toTypedArray()
            AnswerVerifyHelper.verifyAnswer(clazz, testData).also {
                // Save Submit Record
                println(it)
            }
        }
    }
}
