package com.korilin.service

import com.korilin.CompileFailureException
import com.korilin.bo.ContestStatus
import com.korilin.domain.repository.ContestRepository
import com.korilin.domain.repository.InclusionRepository
import com.korilin.domain.repository.QuestionRepository
import com.korilin.domain.repository.RegistrationRepository
import com.korilin.domain.submitRecords
import com.korilin.domain.table.*
import com.korilin.domain.userAnswers
import com.korilin.domain.userProfiles
import com.korilin.model.MyRegister
import com.korilin.model.QuestionAndAnswer
import com.korilin.model.QuestionAnswer
import com.korilin.utils.CodeUtil
import com.korilin.utils.AnswerClassHelper
import com.korilin.utils.AnswerVerifyHelper
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MainService(
    private val visitorService: VisitorService,
    private val registrationRepository: RegistrationRepository,
    private val contestRepository: ContestRepository,
    private val questionRepository: QuestionRepository,
    private val inclusionRepository: InclusionRepository,
    database: Database
) {

    private val userProfiles = database.userProfiles
    private val userAnswers = database.userAnswers
    private val submitRecords = database.submitRecords

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
        val registration =
            registrationRepository.getRegistration(contest.contestId, userId) ?: return Pair("你没有报名该比赛，无法参加答题", null)
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
        block: suspend (Class<*>?, UserProfile, Contest, Question, CompileFailureException?) -> Pair<Boolean, String>
    ): Pair<Boolean, String> {
        val contest = contestRepository.findMainTargetContest()
        if (contest == null || contest.status != ContestStatus.UNDERWAY.id) return Pair(false, "找不到进行中的竞赛")
        val registration =
            registrationRepository.getRegistration(contest.contestId, userId) ?: return Pair(false, "没有报名，无法提交答案")
        val questions = inclusionRepository.getQuestionsDetailByContestId(contest.contestId)
        val question = questions.find { it.questionId == questionId } ?: return Pair(false, "该竞赛没有对应题目")
        val code = CodeUtil.compositeAnswerCode(question.codeTemplate, answer)
        return try {
            val clazz = AnswerClassHelper.createClass(questionId, userId, code)
            block(clazz, registration.user, contest, question, null)
        } catch (e: CompileFailureException) {
            block(null, registration.user, contest, question, e)
        }
    }

    suspend fun testAnswer(userId: Int, questionId: Int, answer: String): Pair<Boolean, String> {
        return pretreatment(userId, questionId, answer) { clazz, _, _, question, e ->
            val testData = question.testDataJson.slice(0..2).map {
                it.toMap()
            }.toTypedArray()
            clazz ?: return@pretreatment Pair(false, "编译错误 ${e?.message}")
            AnswerVerifyHelper.verifyAnswer(clazz, testData, true).let {
                Pair(it.status, it.message)
            }
        }
    }

    suspend fun submitAnswer(userId: Int, questionId: Int, answer: String): Pair<Boolean, String> {
        return pretreatment(userId, questionId, answer) { clazz, user, contest, question, e ->
            val testData = question.testDataJson.map {
                it.toMap()
            }.toTypedArray()
            if (clazz == null) {
                val mes = "编译错误 ${e?.message}"
                submitRecords.add(SubmitRecord {
                    this.question = question
                    this.user = user
                    this.contest = contest
                    this.answer = answer
                    this.pass = 0
                    this.submitTime = LocalDateTime.now()
                    this.elapsedTime = 0
                    this.message = mes
                })
                return@pretreatment Pair(false, mes)
            }
            AnswerVerifyHelper.verifyAnswer(clazz, testData, false).let {
                val pass = ((it.count / testData.size.toFloat()) * 100).toInt()
                // Save Submit Record
                submitRecords.add(SubmitRecord {
                    this.question = question
                    this.user = user
                    this.contest = contest
                    this.answer = answer
                    this.pass = pass
                    this.submitTime = LocalDateTime.now()
                    this.elapsedTime = it.elapsed
                    this.message = it.message
                })
                if (it.status) {
                    Pair(true, it.message)
                } else {
                    Pair(false, "${it.message}, 通过的用例为${pass}%")
                }
            }
        }
    }

    suspend fun getSubmits(userId: Int, questionId: Int) = submitRecords.filter {
        (it.userId eq userId) and (it.questionId eq questionId)
    }.sortedBy {
        it.submitTime
    }.toList().reversed()

    suspend fun getMyContests(userId: Int) =
        registrationRepository.getRegistrationsByUserId(userId).map { registration ->
            val questions = inclusionRepository.getQuestionsDetailByContestId(registration.contest.contestId)
            val rankList = visitorService.getRankList(registration.contest, questions.toTypedArray())
            val rank = rankList.indexOfFirst { it.element1.id == userId } + 1
            MyRegister(registration.contest, rank)
        }
}
