package com.korilin.service

import com.korilin.model.ContestForm
import com.korilin.model.ContestInfo
import com.korilin.repository.ContestRepository
import com.korilin.repository.InclusionRepository
import com.korilin.repository.QuestionRepository
import com.korilin.table.Contest
import com.korilin.table.Inclusion
import com.korilin.table.Question
import javaslang.Tuple2
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

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
}