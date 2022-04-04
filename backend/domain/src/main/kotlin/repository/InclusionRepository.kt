package com.korilin.repository

import com.korilin.bo.TestDataItem
import com.korilin.table.Contest
import com.korilin.table.Inclusion
import com.korilin.table.Inclusions
import com.korilin.table.Question
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.*
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class InclusionRepository(database: Database) {
    private val inclusions = database.inclusions
    private val inclusionSource = database.inclusionsSource

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

    fun addInclusion(contest: Contest, question: Question, sort: Int): Boolean {
        return inclusions.add(Inclusion {
                this.contest= contest
                this.question = question
                this.sort = sort
            }) == 1
    }
}