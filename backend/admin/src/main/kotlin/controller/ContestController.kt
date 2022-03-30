package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.service.ContestService
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
    suspend fun queryContests() : IResponseBody<Array<ContestInfo>> {
        return IResponseBody.success(data = contestService.getAllContestsInfo())
    }
}