package com.korilin.domain.repository

import com.korilin.domain.inclusions
import com.korilin.domain.table.Contest
import com.korilin.domain.table.Inclusion
import com.korilin.domain.table.Question
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.stereotype.Repository

@Repository
class InclusionRepository(database: Database) {
    private val inclusions = database.inclusions

    fun getAllByContestsId(contestId: Int): List<Inclusion> {
        val resultSet = inclusions.filter {
            it.contestId eq contestId
        }.map {
            it.question.apply {
                codeTemplate = "REMOVED"
                description = "REMOVED"
                testDataJson = emptyArray()
            }
            it
        }.sortedBy { it.sort }
        return resultSet
    }

    fun getQuestionsDetailByContestId(contestId: Int): List<Question> {
        val resultSet = inclusions.filter {
            it.contestId eq contestId
        }.sortedBy {
            it.sort
        }.map {
            it.question
        }
        return resultSet
    }

    fun getContestsByQuestionId(questionId: Int): List<Contest> {
        val resultSet = inclusions.filter {
            it.questionId eq questionId
        }.map {
            it.contest.description = "REMOVED"
            it.contest
        }
        return resultSet
    }

    fun addInclusion(contest: Contest, question: Question, sort: Int): Boolean {
        return inclusions.add(Inclusion {
            this.contest = contest
            this.question = question
            this.sort = sort
        }) == 1
    }
}