package com.korilin.controller

import com.korilin.IResponseBody
import com.korilin.UserModuleApiPrefix
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.model.ContestRecord
import com.korilin.service.VisitorService
import com.korilin.domain.table.Contest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UserModuleApiPrefix.VISITOR_PREFIX)
class VisitorController(
    private val visitorService: VisitorService
) {

    @GetMapping("/query/contest/release")
    @ExceptionMessageHandler
    suspend fun getReleaseContest(): IResponseBody<Contest> {
        val contest = visitorService.getReleaseContest()
        return IResponseBody.success(data = contest)
    }

    @GetMapping("/query/contest/record")
    @ExceptionMessageHandler
    suspend fun getContestRecords(): IResponseBody<Array<ContestRecord>> {
        // 笑死
       val data = visitorService.getPublishContests()
        return IResponseBody.success(data = data)
    }
}