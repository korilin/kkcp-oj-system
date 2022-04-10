package com.korilin.controller

import com.korilin.UserModuleApiPrefix
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UserModuleApiPrefix.VISITOR_PREFIX)
class VisitorController {

    @GetMapping("/query/contest/release")
    suspend fun getReleaseContest() {

    }
}