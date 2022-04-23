package com.korilin.domain.repository

import com.korilin.domain.registrations
import com.korilin.domain.table.Contest
import com.korilin.domain.table.Registration
import com.korilin.domain.table.UserProfile
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository

@Repository
class RegistrationRepository(database: Database) {

    private val registrations = database.registrations

    suspend fun register(contest: Contest, user: UserProfile): Boolean {
        val registration = Registration {
            this.contest = contest
            this.user = user
        }
        return registrations.add(registration) == 1
    }

    suspend fun getRegistration(contestId: Int, userId: Int): Registration? {
        return registrations.find {
            (it.contestId eq contestId) and (it.userId eq userId)
        }
    }

    suspend fun getRegistrationsByContestId(contestId: Int): List<Registration> {
        return registrations.filter {
            it.contestId eq contestId
        }.toList()
    }

    suspend fun getRegistrationsByUserId(userId: Int): List<Registration> {
        return registrations.filter {
            it.userId eq userId
        }.toList()
    }
}