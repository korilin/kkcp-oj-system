package com.korilin.controller

import com.korilin.*
import com.korilin.AdminModuleConfig
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.annotations.RegisterExceptionMessage
import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.model.InclusionRequest
import com.korilin.service.ContestService
import com.korilin.domain.table.Question
import com.korilin.model.ContestRegistration
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(AdminModuleConfig.CONTEST_URL_PREFIX)
class ContestController(private val contestService: ContestService) {

    @PostMapping("/new")
    @ExceptionMessageHandler
    @RegisterExceptionMessage(DuplicateKeyException::class, "该名称已经被使用")
    suspend fun newContest(@RequestBody contestForm: ContestForm): IResponseBody<Int> {
        return contestService.createContest(contestForm)?.let {
            IResponseBody.success(data = it)
        } ?: IResponseBody.error("Add Failure")
    }

    @DeleteMapping("/delete")
    suspend fun deleteContest(contestId: Int): IResponseBody<Unit> {
        val result = contestService.deleteContest(contestId)
        return IResponseBody(result, if (result) "" else "Failure", Unit)
    }

    @PutMapping("/update")
    @ExceptionMessageHandler
    @RegisterExceptionMessage(DuplicateKeyException::class, "该名称已经被使用")
    suspend fun updateContest(
        @RequestParam contestId: Int, @RequestBody contestForm: ContestForm
    ): IResponseBody<Unit> {
        val result = contestService.updateContest(contestId, contestForm)
        return IResponseBody(result._1, result._2, null)
    }

    @GetMapping("/query/all")
    suspend fun queryContests(): IResponseBody<Array<ContestInfo>> {
        return IResponseBody.success(data = contestService.getAllContestsInfo())
    }

    @PostMapping("/inclusion/add")
    suspend fun inclusionAdd(@RequestBody inclusionReq: InclusionRequest): IResponseBody<Array<Question>> {
        val (state, messages, questions) = contestService.addInclusion(inclusionReq.contestId, inclusionReq.questions)
        return if (state == -1) IResponseBody.error(messages)
        else IResponseBody(state != 0, messages, questions.toTypedArray())
    }

    @DeleteMapping("/inclusion/remove")
    suspend fun inclusionRemove(contestId: Int, questionId: Int): IResponseBody<Array<Question>> {
        val (result, questions) = contestService.removeInclusion(contestId, questionId)
        return if (result) IResponseBody.success(data = questions.toTypedArray())
        else IResponseBody.error("Can't Remove?")
    }

    @PutMapping("/inclusion/update")
    suspend fun inclusionUpdate(contestId: Int, questionId: Int, offset: Int): IResponseBody<Array<Question>> {
        val questions = contestService.updateInclusionSort(contestId, questionId, offset)
        return IResponseBody.success(data = questions.toTypedArray())
    }

    @PutMapping("/update/status")
    @ExceptionMessageHandler
    @RegisterExceptionMessage(ContestNotFoundException::class, "Could not found the contest!")
    @RegisterExceptionMessage(ContestStatusNotFoundException::class, "Error Contest Status!")
    suspend fun updateStatus(contestId: Int, status: Int): IResponseBody<Int> {
        val newStatus = try {
            contestService.updateStatus(contestId, status)
        } catch (e: AbnormalStatusException) {
            return IResponseBody.error(e.message!!, data = status)
        }
        return IResponseBody(newStatus == status, "", data = newStatus)
    }

    @GetMapping("/registrations")
    @ExceptionMessageHandler
    suspend fun getRegistrations(contestId: Int): IResponseBody<Array<ContestRegistration>> {
        val ls = contestService.getRegistrations(contestId)
        return IResponseBody.success(data = ls.toTypedArray())
    }
}