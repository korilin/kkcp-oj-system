package com.korilin.ktorm

import com.fasterxml.jackson.databind.ObjectMapper
import org.ktorm.entity.Entity

val entityJsonMapper = ObjectMapper().apply {
    findAndRegisterModules()
}

fun Entity<*>.encodeJson() : String = entityJsonMapper.writeValueAsString(this)

inline fun <reified T: Entity<T>> String.decodeJson() : T = entityJsonMapper.readValue(this, T::class.java)