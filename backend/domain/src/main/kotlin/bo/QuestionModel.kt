package com.korilin.bo

/**
 * 问题类型
 */
enum class QuestionType(val id: Int, val text: String) {
    Skills(1, "实用技巧"), Algorithm(2, "算法解题"), Source(3, "源码模拟");

    companion object {
        private fun arrayOf(vararg types: QuestionType) = Array(types.size) { index ->
            val level = types[index]
            mapOf("id" to level.id, "text" to level.text)
        }
        fun toArray() = arrayOf(Skills, Algorithm, Source)
    }
}

/**
 * 问题难度
 */
enum class QuestionLevel(val id: Int, val text: String) {
    Easy(1, "easy"), Medium(2, "medium"), Hard(3, "hard");

    companion object {
        private fun arrayOf(vararg levels: QuestionLevel) = Array(levels.size) { index ->
            val level = levels[index]
            mapOf("id" to level.id, "text" to level.text)
        }
        fun toArray() = arrayOf(Easy, Medium, Hard)
    }
}


data class TestData(
    val input: Any, val output: Any
)