package com.korilin.repository

import com.korilin.table.AdminAccounts
import com.korilin.table.Questions
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.entity.sequenceOf

internal val Database.adminAccounts get() = this.sequenceOf(AdminAccounts)
internal val Database.adminAccountsSource get() = from(AdminAccounts)
internal val Database.questions get() = this.sequenceOf(Questions)
internal val Database.questionsSource get() = from(Questions)
