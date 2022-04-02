package com.korilin.controller

import com.korilin.IResponseBody
import com.korilin.bo.ContestType
import com.korilin.bo.QuestionLevel
import com.korilin.bo.QuestionType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/common")
class CommonController {

    @GetMapping("/question/levels")
    suspend fun questionLevels() = IResponseBody.success(data = QuestionLevel.toArray())

    @GetMapping("/question/types")
    suspend fun questionTypes() = IResponseBody.success(data = QuestionType.toArray())

    @GetMapping("/contest/types")
    suspend fun contestTypes() = IResponseBody.success(data = ContestType.toArray())
}