package com.korilin.repository

import com.korilin.table.Inclusion
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.springframework.stereotype.Repository

@Repository
class InclusionRepository(database: Database) {
    private val inclusions = database.inclusions
    private val inclusionSource = database.inclusionsSource

    fun getAllByContestsId(contestId: Int): Array<Inclusion> {
        val resultSet = inclusions.filter {
            it.contestId eq contestId
        }.toList()
        return resultSet.toTypedArray()
    }

}