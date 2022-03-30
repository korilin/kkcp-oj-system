package com.korilin.repository

import com.korilin.table.AdminAccounts
import com.korilin.table.Contests
import com.korilin.table.Inclusions
import com.korilin.table.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.entity.sequenceOf

internal val Database.adminAccounts get() = this.sequenceOf(AdminAccounts)
internal val Database.adminAccountsSource get() = from(AdminAccounts)
internal val Database.questions get() = this.sequenceOf(Questions)
internal val Database.questionsSource get() = from(Questions)
internal val Database.contests get() = this.sequenceOf(Contests)
internal val Database.contestsSource get() = from(Contests)
internal val Database.inclusions get() = this.sequenceOf(Inclusions)
internal val Database.inclusionsSource get() = from(Inclusions)