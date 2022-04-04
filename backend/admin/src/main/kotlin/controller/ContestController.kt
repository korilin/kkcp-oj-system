package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.model.InclusionRequest
import com.korilin.service.ContestService
import com.korilin.table.Question
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(AdminModuleConfig.CONTEST_URL_PREFIX)
class ContestController(private val contestService: ContestService) {

    @PostMapping("/new")
    suspend fun newContest(@RequestBody contestForm: ContestForm): IResponseBody<Int> {
        return contestService.createContest(contestForm)?.let {
            IResponseBody.success(data = it)
        } ?: IResponseBody.error("添加失败")
    }

    @PutMapping("/update")
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
    suspend fun inclusionRemove(contestId: Int, questionId: Int) =
        if (contestService.removeInclusion(contestId, questionId)) IResponseBody.success(data = Unit)
        else IResponseBody.error("Can't Remove?")
}