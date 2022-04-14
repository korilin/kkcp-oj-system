package com.korilin.repository

import com.korilin.table.Contest
import com.korilin.table.Registration
import com.korilin.table.UserProfile
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.add
import org.ktorm.entity.find
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
            (it.contestId eq contestId) or (it.userId eq userId)
        }
    }
}