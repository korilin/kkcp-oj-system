package com.korilin.bo

enum class ContestType(val id: Int, val text: String) {
    TEST(0, "Test"), PRACTICE(1, "Practice"), ACTIVITY(2, "Activity");

    companion object {
        private fun arrayOf(vararg types: ContestType) = Array(types.size) { index ->
            val type = types[index]
            mapOf("id" to type.id, "text" to type.text)
        }

        fun toArray() = arrayOf(TEST, PRACTICE, ACTIVITY)
    }
}

enum class ContestStatus(val id: Int, val text: String) {
    PLAN(0, "Planning"), RELEASE(1, "Release"), UNDERWAY(2, "Under Way"),
    COMPLETE(3, "Complete"), PUBLISH(4, "Publish");

    companion object {
        private fun arrayOf(vararg statuses: ContestStatus) = Array(statuses.size) { index ->
            val status = statuses[index]
            mapOf("id" to status.id, "text" to status.text)
        }

        fun toArray() = arrayOf(PLAN, RELEASE, UNDERWAY, COMPLETE, PUBLISH)
    }
}