package com.korilin.service

import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.repository.ContestRepository
import com.korilin.repository.InclusionRepository
import com.korilin.table.Contest
import javaslang.Tuple2
import org.springframework.stereotype.Service

@Service
class ContestService(
    private val contestsRepository: ContestRepository,
    private val inclusionRepository: InclusionRepository
) {

    suspend fun getAllContestsInfo(): Array<ContestInfo> {
        val contests = contestsRepository.queryContests()
        return contests.map { contest ->
            val inclusion = inclusionRepository.getAllByContestsId(contest.contestId)
            ContestInfo(contest, inclusion.map { it.question }.toTypedArray())
        }.toTypedArray()
    }

    suspend fun createContest(form: ContestForm): Int? {
        val contest = Contest {
            title = form.title!!
            type = form.type!!
            description = form.description!!
            duration = form.duration!!
            startTime = form.startTime!!
            status = 0
        }
        return if (contestsRepository.newContest(contest)) {
            contest.contestId
        } else null
    }

    suspend fun updateContest(contestId: Int, form: ContestForm): Tuple2<Boolean, String> {
        val contest = contestsRepository.findContestById(contestId) ?: return Tuple2(false, "找不到对应活动")
        form.title?.let { contest.title = it }
        form.type?.let { contest.type = it }
        form.description?.let { contest.description = it }
        form.duration?.let { contest.duration = it }
        form.startTime?.let { contest.startTime = it }
        val result = contest.flushChanges()
        return Tuple2(result == 1, "更新记录 $result 条")
    }
}