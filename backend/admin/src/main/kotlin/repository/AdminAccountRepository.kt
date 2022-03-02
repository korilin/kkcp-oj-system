package com.korilin.repository

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AdminAccountRepository(database: Database) {

    private val adminAccounts = database.adminAccounts

    /**
     * 查询对应 Email 的管理员账号，
     * 更新最后登录时间，并将对象返回
     * @param email 管理员邮箱
     * @return 管理员账号对象
     */
    internal fun adminLogin(email: String) = adminAccounts.run {
        find {
            // 根据 email 查询管理员账号
            it.email eq email
        }?.apply {
            // 更新最后登录时间
            lastLoginTime = LocalDateTime.now()
            flushChanges()
        }
    }
}