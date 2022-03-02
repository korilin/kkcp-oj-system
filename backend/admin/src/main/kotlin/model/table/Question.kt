package com.korilin.model.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

/**
 * 题目实体
 */
interface Question : Entity<Question> {
    companion object : Entity.Factory<Question>()

    val questionId: Int
    var type: Int
    var title: String
    var description: String
    var codeTemplateFilePath: String
    var testDataFilePath: String
    var level: Int // Lv1 ~ Lv5
}

/**
 * 题目表
 */
object Questions: Table<Question>("t_question") {
    val questionId = int("question_id").primaryKey().bindTo { it.questionId }
    val type = int("type").bindTo { it.type }
    val title = varchar("title").bindTo { it.title }
    val description = text("description").bindTo { it.description }
    val codeTemplateFilePath = text("code_template_file_path").bindTo { it.codeTemplateFilePath }
    val testDataFilePath = text("test_data_file_path").bindTo { it.testDataFilePath }
    val level = int("level").bindTo { it.level }
}