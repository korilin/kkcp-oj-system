package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

data class AdminAccount(
    val email: String,
    val name: String,
    val level: Int,
    val lastLoginTime: LocalDateTime,
) {
    constructor(entity: AdminAccountEntity) : this(
        email = entity.email, name = entity.name, level = entity.level, lastLoginTime = entity.lastLoginTime
    )
}

/**
 * 管理员实体
 */
interface AdminAccountEntity : Entity<AdminAccountEntity> {
    companion object : Entity.Factory<AdminAccountEntity>()

    var email: String
    var name: String
    var level: Int
    var lastLoginTime: LocalDateTime
}

/**
 * 管理员账户表
 */
object AdminAccountTable : Table<AdminAccountEntity>("admin_account") {
    val email = varchar("email").primaryKey().bindTo { it.email }
    val name = varchar("name").bindTo { it.name }
    val level = int("level").bindTo { it.level }
    val lastLoginTime = datetime("last_login_time").bindTo { it.lastLoginTime }
}