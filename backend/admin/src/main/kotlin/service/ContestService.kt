package com.korilin.service

import com.korilin.ContestNotFoundException
import com.korilin.ContestStatusNotFoundException
import com.korilin.bo.ContestStatus
import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.repository.ContestRepository
import com.korilin.repository.InclusionRepository
import com.korilin.repository.QuestionRepository
import com.korilin.table.Contest
import com.korilin.table.Question
import com.korilin.toSecond
import javaslang.Tuple2
import kotlinx.coroutines.*
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Service
class ContestService(
    private val contestsRepository: ContestRepository,
    private val questionRepository: QuestionRepository,
    private val inclusionRepository: InclusionRepository
) {

    suspend fun getAllContestsInfo(): Array<ContestInfo> {
        val contests = contestsRepository.queryContests()
        return contests.map { contest ->
            val inclusions = inclusionRepository.getAllByContestsId(contest.contestId)
            ContestInfo(contest, inclusions.map { it.question }.toTypedArray())
        }.toTypedArray()
    }

    @Transactional
    suspend fun deleteContest(contestId: Int): Boolean {
        val contest = contestsRepository.findContestById(contestId) ?: return false
        var optCount = 0
        val inclusions = inclusionRepository.getAllByContestsId(contestId)
        inclusions.forEach {
            optCount += it.delete()
        }
        optCount += contest.delete()
        assert(optCount == inclusions.size + 1)
        return true
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

    suspend fun updateContest(@RequestParam contestId: Int, @RequestBody form: ContestForm): Tuple2<Boolean, String> {
        val contest = contestsRepository.findContestById(contestId) ?: return Tuple2(false, "找不到对应活动")
        form.title?.let { contest.title = it }
        form.type?.let { contest.type = it }
        form.description?.let { contest.description = it }
        form.duration?.let { contest.duration = it }
        form.startTime?.let { contest.startTime = it }
        val result = contest.flushChanges()
        return Tuple2(result == 1, "更新记录 $result 条")
    }

    @Transactional
    suspend fun addInclusion(contestId: Int, questions: Array<Int>): Triple<Int, String, List<Question>> {
        val messages = StringBuilder()
        val contest = contestsRepository.findContestById(contestId) ?: return run {
            messages.append("The contest#$contestId could not be found!\n")
            Triple(-1, messages.toString(), emptyList())
        }
        var count = inclusionRepository.getAllByContestsId(contestId).size
        var optCount = 0
        for (questionId in questions) {
            val question = questionRepository.findQuestionById(questionId)
            if (question == null) {
                messages.append("The question#$questionId could not be found!\n")
                continue
            }
            try {
                inclusionRepository.addInclusion(contest, question, count++)
                optCount++
            } catch (dke: DuplicateKeyException) {
                messages.append("The question#$questionId named ${question.title} already included in this contest.\n")
            }
        }
        if (optCount == questions.size) messages.append("All $optCount Questions Add Success!")
        val inclusions = inclusionRepository.getAllByContestsId(contest.contestId)
        return Triple(optCount, messages.toString(), inclusions.map { it.question })
    }

    @Transactional
    suspend fun removeInclusion(contestId: Int, questionId: Int): Pair<Boolean, List<Question>> {
        var del = false
        val questions = mutableListOf<Question>()
        val inclusions = inclusionRepository.getAllByContestsId(contestId)
        inclusions.forEach {
            if (it.question.questionId == questionId) {
                it.delete()
                del = true
            } else {
                questions.add(it.question)
            }
            if (del) {
                it.sort = it.sort - 1
                it.flushChanges()
            }
        }
        return Pair(del, questions)
    }

    @Transactional
    suspend fun updateInclusionSort(contestId: Int, questionId: Int, offset: Int): List<Question> {
        val inclusions = inclusionRepository.getAllByContestsId(contestId)
        for (index in inclusions.indices) {
            val now = inclusions[index]
            if (now.question.questionId == questionId) {
                inclusions.getOrNull(index + offset)?.let { target ->
                    target.sort = now.sort.also { now.sort = target.sort }
                    target.flushChanges()
                    now.flushChanges()
                }
                break
            }
        }
        return inclusionRepository.getAllByContestsId(contestId).map { it.question }
    }

    suspend fun updateStatus(contestId: Int, statusId: Int): Int {
        val contest = contestsRepository.findContestById(contestId) ?: throw ContestNotFoundException()
        val old = contest.status
        val status = ContestStatus[statusId] ?: throw ContestStatusNotFoundException(statusId)
        val result = withContext(Dispatchers.IO) {
            contestsRepository.updateStatus(contest, status)
        }
        if (result == 0) return old
        val oldStatus = ContestStatus[old]
        if (oldStatus == ContestStatus.RELEASE || oldStatus == ContestStatus.UNDERWAY) {
            cancelTask()
        }
        if (status == ContestStatus.RELEASE) {
            nextRelease(contestId, contest.startTime)
        } else if (status == ContestStatus.UNDERWAY) {
            nextUnderway(contestId, contest.startTime, contest.duration)
        }
        return contest.status
    }

    private var taskScope: CoroutineScope? = null

    private fun cancelTask() {
        taskScope?.cancel()
        taskScope = null
    }

    /**
     * @param interval 单位：秒
     */
    private suspend fun setTask(interval: Long, block: suspend () -> Unit) {
        cancelTask()
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            println("Do Task Delay: $interval second")
            delay(interval * 1000L)
            block()
            println("Task Complete")
        }
    }

    /**
     * - 更新 [Contest] 状态为 [ContestStatus.UNDERWAY]
     * - 开启通知邮件推送 TODO
     */
    private suspend fun nextRelease(contestId: Int, startTime: LocalDateTime) {
        val now = LocalDateTime.now()
        var interval = startTime.toSecond() - now.toSecond()
        if (interval < 0) interval = 0
        setTask(interval) {
            updateStatus(contestId, ContestStatus.UNDERWAY.id)
        }
    }

    /**
     * - 更新 [Contest] 状态为 [ContestStatus.COMPLETE]
     */
    private suspend fun nextUnderway(contestId: Int, startTime: LocalDateTime, duration: Int) {
        var interval = startTime.toSecond() + duration * 60 - LocalDateTime.now().toSecond()
        if(interval < 0) interval = 0
        setTask(interval) {
            updateStatus(contestId, ContestStatus.COMPLETE.id)
        }
    }
}
