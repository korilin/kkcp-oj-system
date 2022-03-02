package com.korilin.model.vo

import com.korilin.model.table.Question

data class QuestionModel(
    val questionId: Int,
    val type: QuestionType,
    val title: String,
    val description: String,
    val codeTemplateFileLastUploadTime: String,
    val testDataFileLastUploadTime: String,
    val level: QuestionLevel
) {
    companion object {
        fun fromEntity(entity: Question) = QuestionModel(
            entity.id.value,
            QuestionType.byId(entity.type),
            entity.title,
            entity.description,
            entity.codeTemplateFilePath,
            entity.testDataFilePath,
            QuestionLevel.byLevel(entity.level)
        )
    }
}

enum class QuestionType(val id: Int, val text: String) {
    TP0(0, "0"), TP1(1, "1"), TP2(2, "2"), TP3(3, "3");

    companion object {
        fun byId(id: Int) = when (id) {
            1 -> TP1
            2 -> TP2
            3 -> TP3
            else -> TP0
        }
    }
}

enum class QuestionLevel(val level: Int, val text: String) {
    NoFound(0, ""), Easy(1, "easy"), Medium(2, "medium"), Hard(3, "hard");

    companion object {
        fun byLevel(level: Int) = when (level) {
            1 -> Easy
            2 -> Medium
            3 -> Hard
            else -> NoFound
        }
    }
}