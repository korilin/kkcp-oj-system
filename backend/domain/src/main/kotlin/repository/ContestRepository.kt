package com.korilin.domain.repository

import com.korilin.AbnormalStatusException
import com.korilin.bo.ContestStatus
import com.korilin.domain.contests
import com.korilin.domain.contestsSource
import com.korilin.domain.table.Contest
import com.korilin.domain.table.Contests
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.sortedBy
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ContestRepository(database: Database) {
    private val contests = database.contests
    private val contestsSource = database.contestsSource

    fun findContestById(contestId: Int) = contests.find {
        it.contestId eq contestId
    }

    fun queryContests() = contestsSource.select(Contests.columns).map {
        Contests.createEntity(it)
    }

    fun queryPublicContests() = contests.filter {
        it.status eq ContestStatus.PUBLISH.id
    }.sortedBy {
        it.startTime
    }

    fun newContest(contest: Contest) = contests.add(contest) == 1

    fun findMainTargetContest() = contests.find {
        (it.status eq ContestStatus.RELEASE.id) or (it.status eq ContestStatus.UNDERWAY.id) or (it.status eq ContestStatus.COMPLETE.id)
    }

    @Transactional
    suspend fun updateStatus(contest: Contest, status: ContestStatus): Int {
        if (status == ContestStatus.RELEASE || status == ContestStatus.UNDERWAY) {
            synchronized(this) {
                val f = contests.find {
                    (it.status eq ContestStatus.RELEASE.id) or (it.status eq ContestStatus.UNDERWAY.id)
                }
                if (f != null && f.contestId != contest.contestId) {
                    throw AbnormalStatusException("There has a ${status.text} contest ${f.contestId}_${f.title}")
                }
            }
        }
        contest.status = status.id
        return contest.flushChanges()
    }
}

