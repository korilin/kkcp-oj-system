package com.korilin.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.korilin.table.AdminAccountTable
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf

private val entityJsonMapper = ObjectMapper().apply {
    findAndRegisterModules()
}

fun Entity<*>.encodeJson(): String = entityJsonMapper.writeValueAsString(this)

val Database.adminAccounts get() = this.sequenceOf(AdminAccountTable)