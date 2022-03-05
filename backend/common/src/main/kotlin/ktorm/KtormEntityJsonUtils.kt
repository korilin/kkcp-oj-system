package com.korilin.ktorm

import com.fasterxml.jackson.databind.ObjectMapper
import org.ktorm.entity.Entity

val globalJsonMapper = ObjectMapper().apply {
    findAndRegisterModules()
}

fun Entity<*>.encodeJson() : String = globalJsonMapper.writeValueAsString(this)

inline fun <reified T: Entity<T>> String.decodeJson() : T = globalJsonMapper.readValue(this, T::class.java)