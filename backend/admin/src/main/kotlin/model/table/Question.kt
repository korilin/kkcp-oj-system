package com.korilin.model.table

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column


/**
 * 题目实体
 */
class Question(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Question>(Questions)

    var type: Int by Questions.type
    var title: String by Questions.title
    var description: String by Questions.description
    var codeTemplateFilePath: String by Questions.codeTemplateFilePath
    var testDataFilePath: String by Questions.testDataFilePath
    var level: Int by Questions.level // Lv1 ~ Lv5
}

/**
 * 题目表
 */
object Questions : IdTable<Int>("t_question") {

    override val id = integer("question_id").autoIncrement().entityId()
    val type = integer("type")
    val title = varchar("title", 45).uniqueIndex()
    val description = text("description")
    val codeTemplateFilePath = text("code_template_file_path")
    val testDataFilePath = text("test_data_file_path")
    val level = integer("level")

    override val primaryKey = PrimaryKey(id)
}