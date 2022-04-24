package com.korilin.service

import com.korilin.domain.adminOptions
import com.korilin.domain.repository.AdminAccountRepository
import com.korilin.domain.table.UserProfile
import com.korilin.domain.userProfiles
import org.ktorm.database.Database
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val adminAccountRepository: AdminAccountRepository,
    database: Database
){

    private val userProfiles = database.userProfiles
    private val adminOpts = database.adminOptions


}