package com.korilin.bo

import java.lang.IndexOutOfBoundsException

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

/**
 * - [PLAN] 在准备状态，用户不会看到
 * - [RELEASE] 发布状态，将作为当前活动显示在主页，同一时间只能有一个 Contest 处于发布状态
 * - [UNDERWAY] 进行中状态，由系统定时器在 [RELEASE] 的 Contest 到达 Start Time 时自动更新，或由管理员手动更
 * - [COMPLETE] 当 [UNDERWAY] 状态的 Contest duration 耗尽时进入 [COMPLETE] 状态，用户无法再进入 Contest 进行答题
 * - [PUBLISH] 由管理员完成数据审核后手动切换到该状态，公布该 Contest 的参与数据和排名信息
 */
enum class ContestStatus(val id: Int, val text: String, val desc: String ="") {
    PLAN(0, "Planning", "User will not see the contest."),
    RELEASE(1, "Release", "The contest will be display on the site. Only one contest can be in the release status!"),
    UNDERWAY(2, "Under Way", "The contest will open the question entry."),
    COMPLETE(3, "Complete", "The contest will close the question entry."),
    PUBLISH(4, "Publish", "The contest will show in publish record.");

    companion object {
        private fun arrayOf(vararg statuses: ContestStatus) = Array(statuses.size) { index ->
            val status = statuses[index]
            mapOf("id" to status.id, "text" to status.text, "updateDesc" to status.desc)
        }

        fun toArray() = arrayOf(PLAN, RELEASE, UNDERWAY, COMPLETE, PUBLISH)

        operator fun get(id: Int) = when (id) {
            0 -> PLAN
            1 -> RELEASE
            2 -> UNDERWAY
            3 -> COMPLETE
            4 -> PUBLISH
            else -> null
        }
    }
}