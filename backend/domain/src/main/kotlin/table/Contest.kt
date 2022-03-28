package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

/**
 * 竞赛信息实体
 */
interface Contest: Entity<Contest> {
    companion object : Entity.Factory<Contest>()

    val contestId: Int
    var title: String
    var type: Int
    var description: String // html
    var duration: Int // second
    var startTime: LocalDateTime
    var status: Int // extendability status type
}

/**
 * 竞赛活动表
 */
object Contests : Table<Contest>("t_contest") {
    val contestId = int("contest_id").primaryKey().bindTo { it.contestId }
    val title = varchar("title").bindTo { it.title }
    val type = int("type").bindTo { it.type }
    val description = text("description").bindTo { it.description }
    val duration = int("duration").bindTo { it.duration }
    val startTime = datetime("start_time").bindTo { it.startTime }
    val status = int("status").bindTo { it.status }
}