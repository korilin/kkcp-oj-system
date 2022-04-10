package com.korilin.service

import com.korilin.repository.ContestRepository
import com.korilin.table.Contest
import org.springframework.stereotype.Service

@Service
class VisitorService(
    private val contestRepository: ContestRepository
) {

    fun getReleaseContest(): Contest? {
        return contestRepository.findReleaseContest()
    }
}
