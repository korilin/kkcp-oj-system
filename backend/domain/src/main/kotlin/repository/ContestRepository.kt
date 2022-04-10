package com.korilin.repository

import com.korilin.AbnormalStatusException
import com.korilin.bo.ContestStatus
import com.korilin.table.Contest
import com.korilin.table.Contests
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.or
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.ktorm.entity.find
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

    fun newContest(contest: Contest) = contests.add(contest) == 1

    fun findReleaseContest() = contests.find {
        it.status eq ContestStatus.RELEASE.id
    }

    @Transactional
    suspend fun updateStatus(contest: Contest, status: ContestStatus): Int {
        if (status == ContestStatus.RELEASE || status == ContestStatus.UNDERWAY) {
            synchronized(this) {
                val f = contests.find {
                    (it.status eq ContestStatus.RELEASE.id) or (it.status eq ContestStatus.UNDERWAY.id)
                }
                if (f != null) {
                    throw AbnormalStatusException("There has a ${status.text} contest ${f.contestId}_${f.title}")
                }
            }
        }
        contest.status = status.id
        return contest.flushChanges()
    }
}

