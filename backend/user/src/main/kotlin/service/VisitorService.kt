package com.korilin.service

import com.korilin.repository.ContestRepository
import com.korilin.table.Contest

class VisitorService(
    private val contestRepository: ContestRepository
) {

    suspend fun getReleaseContest(): Contest {
        TODO()
    }
}
