package com.korilin.bo

enum class ContestType(val id: Int, val text: String) {
    TEST(0, "Test"), PRACTICE(1, "Practice"), ACTIVITY(2, "Activity");

    companion object {
        private fun arrayOf(vararg levels: ContestType) = Array(levels.size) { index ->
            val level = levels[index]
            mapOf("id" to level.id, "text" to level.text)
        }

        fun toArray() = arrayOf(TEST, PRACTICE, ACTIVITY)
    }
}