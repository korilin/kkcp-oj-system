package com.korilin.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.korilin.model.table.AdminAccounts
import com.korilin.model.table.Questions
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf

// 为 ObjectMapper 注册 KtormModule
val entityJsonMapper = ObjectMapper().apply {
    findAndRegisterModules()
}

// 序列化为 JSON
fun Entity<*>.encodeJson(): String = entityJsonMapper.writeValueAsString(this)

// 反序列化为对象
inline fun <reified T : Entity<T>> String.decodeJson(): T = entityJsonMapper.readValue(this, T::class.java)

// 数据表序列化
val Database.adminAccounts get() = this.sequenceOf(AdminAccounts)
val Database.questions get() = this.sequenceOf(Questions)