package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface Registration : Entity<Registration> {
    companion object : Entity.Factory<Registration>()

    var contest: Contest
    var user: UserProfile
    var attachId: Int
}

object Registrations : Table<Registration>("contest_registration") {
    val contestId = int("contest_id").primaryKey().references(Contests) { it.contest }
    val userId = int("user_id").primaryKey().references(UserProfiles) { it.user }
    val attachId = int("attach_id").bindTo { it.attachId }
}
