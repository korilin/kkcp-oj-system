package com.korilin.service

import com.korilin.domain.repository.ContestRepository
import com.korilin.domain.repository.InclusionRepository
import com.korilin.domain.repository.RegistrationRepository
import com.korilin.domain.submitRecords
import com.korilin.domain.table.Contest
import com.korilin.domain.table.SubmitRecord
import com.korilin.domain.userAnswers
import com.korilin.domain.userProfiles
import com.korilin.model.ContestRecord
import com.korilin.model.RankInfo
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VisitorService(
    private val contestRepository: ContestRepository,
    private val inclusionRepository: InclusionRepository,
    private val registrationRepository: RegistrationRepository,
    database: Database
) {

    private val userProfiles = database.userProfiles
    private val userAnswers = database.userAnswers
    private val submitRecords = database.submitRecords

    fun getReleaseContest(): Contest? {
        return contestRepository.findMainTargetContest()
    }

    suspend fun getPublishContests(): Array<ContestRecord> {
        // 外层所有 contest
        return contestRepository.queryPublicContests().map { contest ->
            val questions = inclusionRepository.getAllByContestsId(contest.contestId).map { it.question }.toTypedArray()
           // 内层获取报名的用户
            val rank = registrationRepository.getRegistrations(contest.contestId).map {registration ->
                // 每个用户的活动数据
                // 成绩
                var count = 0
                // 所有最早完成时间的最晚时间
                var lastTime: LocalDateTime? = null
                // 所有问题的答案
                val answers = mutableListOf<SubmitRecord?>()
                for (question in questions) {
                    val temp1 = submitRecords.filter {
                        (it.userId eq registration.user.id) and (it.questionId eq question.questionId)
                    }.sortedBy { it.pass }.toList()
                    if (temp1.isEmpty()) {
                        answers.add(null)
                        continue
                    }
                    val bigger = temp1.last()
                    val temp2 = temp1.filter { it.pass == bigger.pass }.sortedBy { it.submitTime }
                    // 该问题最早完成的时间
                    val best = temp2.first()
                    lastTime = if (lastTime == null) {
                        best.submitTime
                    }else{
                        maxOf(lastTime, best.submitTime)
                    }
                    count += best.pass
                    answers.add(best)
                }
                // 可能没答题
                Tuple4(registration.user, count, lastTime ?: LocalDateTime.now(), answers)
            }.sortedWith { o1, o2 ->
                // 现按照成绩做比较，再按照时间做比较
                if (o1.element2 == o2.element2) {
                    o1.element3.compareTo(o2.element3)
                } else {
                    o2.element2.compareTo(o1.element2)
                }
            }.map {
                RankInfo(it.element1, it.element4.toTypedArray())
            }.toTypedArray()
            ContestRecord(contest, questions, rank)
        }.toTypedArray()
    }
}
