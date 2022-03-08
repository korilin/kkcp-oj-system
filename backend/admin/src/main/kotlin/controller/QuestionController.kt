package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.annotations.RegisterExceptionMessage
import com.korilin.model.NewQuestionForm
import com.korilin.service.QuestionService
import com.korilin.table.Question
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(AdminModuleConfig.QUESTION_URL_PREFIX)
internal class QuestionController(private val questionService: QuestionService) {

    @GetMapping("/query/all")
    @ExceptionMessageHandler
    suspend fun queryAllQuestions(): IResponseBody<List<Question>> {
        val questions = questionService.getAllQuestions()
        return IResponseBody.success(data = questions)
    }

    @PostMapping("/new")
    @ExceptionMessageHandler
    @RegisterExceptionMessage(DuplicateKeyException::class, "该题目名称已经被使用")
    suspend fun newQuestion(@RequestBody form: NewQuestionForm): IResponseBody<Int> {
        return questionService.newQuestion(form)?.let {
            IResponseBody.success(data = it)
        } ?: IResponseBody.error("")
    }
}