package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

/**
 * 管理员账户表
 */
object AdminAccounts : Table<AdminAccount>("admin_account") {
    val email = varchar("email").primaryKey().bindTo { it.email }
    val name = varchar("name").bindTo { it.name }
    val level = int("level").bindTo { it.level }
    val lastLoginTime = datetime("last_login_time").bindTo { it.lastLoginTime }
}

/**
 * 管理员实体
 */
interface AdminAccount: Entity<AdminAccount> {
    companion object : Entity.Factory<AdminAccount>()

    var email: String
    var name: String
    var level: Int
    var lastLoginTime: LocalDateTime
}