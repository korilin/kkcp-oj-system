package com.korilin.model.table

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 管理员账户表
 */
object AdminAccounts : IntIdTable("admin_account") {
    val email = varchar("email", 255).uniqueIndex()
    val name = varchar("name", 45)
    val level = integer("level")
    val lastLoginTime = datetime("last_login_time")
}

/**
 * 管理员实体
 */
class AdminAccount(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AdminAccount>(AdminAccounts)

    var email: String by AdminAccounts.email
    var name: String by AdminAccounts.name
    var level: Int by AdminAccounts.level
    var lastLoginTime: LocalDateTime by AdminAccounts.lastLoginTime
}
