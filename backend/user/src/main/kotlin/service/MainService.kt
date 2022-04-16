package com.korilin.service

import com.korilin.bo.ContestStatus
import com.korilin.model.QuestionAndAnswer
import com.korilin.repository.*
import com.korilin.table.Registration
import com.korilin.table.UserAnswer
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Service

const val CODE_REGEX = """
// user start
(.*)
// user end
"""

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
            question.codeTemplate = getUserCodeTemplate(question.codeTemplate)
            question.testDataJson = question.testDataJson.slice(0..2).toTypedArray()
            var userAnswer = userAnswers.find { it.questionId eq question.questionId }
            if (userAnswer == null) {
                userAnswer = UserAnswer {
                    this.question = question
                    this.answer = question.codeTemplate
                    this.attachId = registration.attachId
                }
                userAnswers.add(userAnswer)
            }
            QuestionAndAnswer(question, userAnswer)
        }
        return answers.toTypedArray()
    }

    suspend fun getUserCodeTemplate(codeTemplate: String): String {
        val regex = Regex(CODE_REGEX)
        val result = regex.find(codeTemplate)
        return result?.value ?: ""
    }
}
