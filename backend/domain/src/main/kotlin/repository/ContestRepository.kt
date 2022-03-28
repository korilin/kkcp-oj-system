package com.korilin.repository

import org.ktorm.database.Database
import org.springframework.stereotype.Repository

@Repository
class ContestRepository(database: Database) {
    private val contests = database.contests
    private val contestsSource = database.contestsSource
}