package com.korilin.table

import com.korilin.bo.TestData
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
    var codeTemplateFilePath: String
    var codeTemplateFileLastUploadTime: LocalDateTime
    var testData: List<TestData>
    var testDataLastUpdateTime: LocalDateTime
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
    val description = text("description").bindTo { it.description }
    val codeTemplateFilePath = text("code_template_file_path").bindTo { it.codeTemplateFilePath }
    val codeTemplateFileLastUploadTime =
        datetime("code_template_file_last_upload_time").bindTo { it.codeTemplateFileLastUploadTime }
    val testData = json<List<TestData>>("test_data").bindTo { it.testData }
    val testDataLastUpdateTime = datetime("test_data_last_update_time").bindTo { it.testDataLastUpdateTime }
}