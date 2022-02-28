package com.korilin.config

import com.fasterxml.jackson.databind.Module
import org.ktorm.database.Database
import org.ktorm.jackson.KtormModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class KtormConfiguration(private val dataSource: DataSource) {
    @Bean
    fun database(): Database = Database.connectWithSpringSupport(dataSource)

    @Bean
    fun ktormModule(): Module = KtormModule()
}