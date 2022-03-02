package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.model.table.Question
import com.korilin.model.vo.QuestionModel
import com.korilin.service.QueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AdminModuleConfig.QUERY_URL_PREFIX)
internal class QueryController(private val queryService: QueryService) {

    @GetMapping("/question/all")
    @ExceptionMessageHandler
    suspend fun queryAllQuestions(): IResponseBody<List<QuestionModel>> {
        val questions = queryService.getAllQuestions()
        return IResponseBody.success(data = questions)
    }
}