package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import java.time.LocalDateTime

interface Registration : Entity<Registration> {
    companion object : Entity.Factory<Registration>()

    var contest: Contest
    var user: UserProfile
    var attachId: Int
    var answerLastUpdateTime: LocalDateTime?
}

object Registrations : Table<Registration>("contest_registration") {
    val contestId = int("contest_id").primaryKey().references(Contests) { it.contest }
    val userId = int("user_id").primaryKey().references(UserProfiles) { it.user }
    val attachId = int("attach_id").bindTo { it.attachId }
    val answerLastUpdateTime = datetime("answer_last_update_time").bindTo { it.answerLastUpdateTime }
}
