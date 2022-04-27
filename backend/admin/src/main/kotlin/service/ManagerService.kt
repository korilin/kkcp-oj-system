package com.korilin.service

import com.korilin.domain.adminOptions
import com.korilin.domain.repository.AdminAccountRepository
import com.korilin.domain.table.AdminAccount
import com.korilin.domain.table.UserProfile
import com.korilin.domain.userProfiles
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.notEq
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val adminAccountRepository: AdminAccountRepository,
    database: Database
) {

    private val userProfiles = database.userProfiles
    private val adminOpts = database.adminOptions

    suspend fun allUser() = userProfiles.filter { it.id notEq -1 }.toList()

    suspend fun blockUser(userId: Int, status: Boolean): Boolean {
        val user = userProfiles.find { it.id eq userId } ?: return false
        user.block = status
        return user.flushChanges() == 1
    }

    suspend fun allOpts() = adminOpts.filter { it.option notEq "" }.toList()

    suspend fun allAccount() = adminAccountRepository.queryAllAdmin()

    suspend fun addAccount(account: AdminAccount) = adminAccountRepository.newAdminAccount(account) == 1

    suspend fun deleteAccount(email: String): Boolean {
        val account = adminAccountRepository.queryAdminAccount(email)
        return account?.delete() == 1
    }
}