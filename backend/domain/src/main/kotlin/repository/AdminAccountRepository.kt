package com.korilin.domain.repository

import com.korilin.domain.adminAccounts
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.springframework.stereotype.Repository

@Repository
class AdminAccountRepository(database: Database) {

    private val adminAccounts = database.adminAccounts

    suspend fun queryAdminAccount(email: String) = adminAccounts.find {
        it.email eq email
    }
}