package com.korilin.bo

/**
 * 问题类型
 */
enum class QuestionType(val id: Int, val text: String) {
    TP1(1, "实用技巧"), TP2(2, "算法解题"), TP3(3, "源码模拟");
}

/**
 * 问题难度
 */
enum class QuestionLevel(val level: Int, val text: String) {
    Easy(1, "easy"), Medium(2, "medium"), Hard(3, "hard");
}


data class TestData(
    val input: Any,
    val output: Any
)