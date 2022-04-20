package com.korilin.domain.table

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

interface SubmitRecord : Entity<SubmitRecord> {

    companion object : Entity.Factory<SubmitRecord>()

    val submitId: Int
    var question: Question
    var user: UserProfile
    var contest: Contest?
    var answer: String
    var pass: Int
    var submitTime: LocalDateTime
    var elapsedTime: Int // 执行时长
}

object SubmitRecords : Table<SubmitRecord>("t_submit_record") {

    val submitId = int("submit_id").primaryKey().bindTo { it.submitId }
    val questionId = int("question_id").references(Questions) { it.question }
    val userId = int("user_id").references(UserProfiles) { it.user }
    val contestId = int("contest_id").references(Contests) { it.contest }
    val answer = text("answer").bindTo { it.answer }
    val isPass = int("pass").bindTo { it.pass }
    val submitTime = datetime("submit_time").bindTo { it.submitTime }
    val elapsedTime = int("elapsed_time").bindTo { it.elapsedTime }
}
