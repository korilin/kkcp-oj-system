package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface Inclusion : Entity<Inclusion> {
    companion object : Entity.Factory<Inclusion>()

    var contest: Contest
    var question: Question
    var sort: Int
}

object Inclusions: Table<Inclusion>("t_inclusion") {
    val contestId = int("contest_id").references(Contests) { it.contest }
    val questionId = int("question_id").references(Questions) { it.question }
    val sort = int("sort").bindTo { it.sort }
}
