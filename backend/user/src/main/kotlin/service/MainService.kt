package com.korilin.service

import com.korilin.repository.ContestRepository
import com.korilin.repository.RegistrationRepository
import com.korilin.repository.userProfiles
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.springframework.stereotype.Service

@Service
class MainService(
    private val registrationRepository: RegistrationRepository,
    private val contestRepository: ContestRepository,
    database: Database
) {

    private val userProfiles = database.userProfiles

    suspend fun register(contestId: Int, userId: Int): Pair<Boolean, String> {
        if(registrationRepository.getRegistration(contestId, userId) != null) {
            return Pair(false, "已报名过了")
        }
        val contest = contestRepository.findContestById(contestId) ?: throw NullPointerException("找不到对应活动")
        val user = userProfiles.find { it.id eq userId } ?: throw NullPointerException("找不到对应用户")
        val result = registrationRepository.register(contest, user)
        return Pair(result, "原因不明")
    }
}