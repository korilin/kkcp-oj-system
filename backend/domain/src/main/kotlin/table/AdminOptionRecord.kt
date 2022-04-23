package com.korilin.domain.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface AdminOptionRecord: Entity<AdminOptionRecord> {

    companion object : Entity.Factory<AdminOptionRecord>()

    val optId: Int
    var email: String
    var option: String
    var time: LocalDateTime
}

object AdminOptionRecords: Table<AdminOptionRecord>("t_admin_option_record") {
    val optId = int("opt_id").primaryKey().bindTo { it.optId }
    val email = varchar("email").bindTo { it.email }
    val option = varchar("option").bindTo { it.option }
    val time = datetime("time").bindTo { it.time }
}
