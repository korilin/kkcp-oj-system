package com.korilin.service

import com.korilin.domain.repository.ContestRepository
import com.korilin.domain.table.Contest
import org.springframework.stereotype.Service

@Service
class VisitorService(
    private val contestRepository: ContestRepository
) {

    fun getReleaseContest(): Contest? {
        return contestRepository.findMainTargetContest()
    }
}
