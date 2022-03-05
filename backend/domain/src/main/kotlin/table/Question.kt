package com.korilin.table

import com.korilin.bo.TestDataItem
import org.ktorm.entity.Entity
import org.ktorm.jackson.json
import org.ktorm.schema.*
import java.time.LocalDateTime


/**
 * 题目实体
 */
interface Question : Entity<Question> {
    companion object : Entity.Factory<Question>()

    val questionId: Int
    var type: Int
    var title: String
    var description: String
    var codeTemplate: String
    var codeTemplateLastUpdateTime: LocalDateTime
    var testJsonData: Array<TestDataItem>
    var testJsonDataLastUpdateTime: LocalDateTime
    var level: Int // Lv1 ~ Lv5
}

/**
 * 题目表
 */
object Questions : Table<Question>("t_question") {

    val questionId = int("question_id").primaryKey().bindTo { it.questionId }
    val type = int("type").bindTo { it.type }
    val level = int("level").bindTo { it.level }
    val title = varchar("title").bindTo { it.title }
    val description = text("description").bindTo { it.description } // 65,535 bytes
    val codeTemplate = text("code_template").bindTo { it.codeTemplate }
    val codeTemplateLastUpdateTime =
        datetime("code_template_last_update_time").bindTo { it.codeTemplateLastUpdateTime }
    val testJsonData = json<Array<TestDataItem>>("test_json_data").bindTo { it.testJsonData }
    val testJsonDataLastUpdateTime =
        datetime("test_json_data_last_update_time").bindTo { it.testJsonDataLastUpdateTime }
}