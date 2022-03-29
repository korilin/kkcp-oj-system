package com.korilin.repository

import com.korilin.table.Contest
import com.korilin.table.Contests
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.stereotype.Repository

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

}