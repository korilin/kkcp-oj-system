package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.model.NewQuestionForm
import com.korilin.service.QuestionService
import com.korilin.table.Question
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AdminModuleConfig.QUESTION_URL_PREFIX)
internal class QuestionController(private val questionService: QuestionService) {

    @GetMapping("/query/all")
    @ExceptionMessageHandler
    suspend fun queryAllQuestions(): IResponseBody<List<Question>> {
        val questions = questionService.getAllQuestions()
        return IResponseBody.success(data = questions)
    }
}