package com.korilin.domain

import com.korilin.domain.table.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.entity.sequenceOf

internal val Database.adminAccounts get() = this.sequenceOf(AdminAccounts)
internal val Database.questions get() = this.sequenceOf(Questions)
internal val Database.questionsSource get() = from(Questions)
internal val Database.contests get() = this.sequenceOf(Contests)
internal val Database.contestsSource get() = from(Contests)
internal val Database.inclusions get() = this.sequenceOf(Inclusions)
internal val Database.registrations get() = this.sequenceOf(Registrations)

val Database.userProfiles get() = this.sequenceOf(UserProfiles)
val Database.userAnswers get() = this.sequenceOf(UserAnswers)
val Database.submitRecords get() = this.sequenceOf(SubmitRecords)
val Database.adminOptions get() = this.sequenceOf(AdminOptionRecords)