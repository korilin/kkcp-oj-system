package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.annotations.RegisterExceptionMessage
import com.korilin.model.NewQuestionForm
import com.korilin.model.QuestionDetail
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

    @GetMapping("/query/detail")
    @ExceptionMessageHandler
    suspend fun queryQuestionDetail(questionId: Int): IResponseBody<QuestionDetail> {
        questionService.getQuestionDetail(questionId = questionId)?.let {
            return IResponseBody.success(data = it)
        }
        return IResponseBody.error("获取不到对应 $questionId 的问题详情")
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