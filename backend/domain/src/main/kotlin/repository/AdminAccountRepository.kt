package com.korilin.domain.repository

import com.korilin.domain.adminAccounts
import org.ktorm.database.Database
import org.ktorm.dsl.between
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository

@Repository
class AdminAccountRepository(database: Database) {

    private val adminAccounts = database.adminAccounts

    suspend fun queryAdminAccount(email: String) = adminAccounts.find {
        it.email eq email
    }

    suspend fun queryAllAdmin() = adminAccounts.filter { it.level between 1..3 }.toList()
}