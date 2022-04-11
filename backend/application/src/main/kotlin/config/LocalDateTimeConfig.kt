package com.korilin.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.korilin.LOCAL_DATE_TIME_PATTERN
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class LocalDateTimeConfig {

    @Bean
    fun formatter(): Module {
        return JavaTimeModule().apply {
            val jClass = LocalDateTime::class.java
            val formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)
            // yyyy-MM-dd HH:mm:ss
            addDeserializer(jClass, LocalDateTimeDeserializer(formatter))
            addSerializer(jClass, LocalDateTimeSerializer(formatter))
        }
    }
}