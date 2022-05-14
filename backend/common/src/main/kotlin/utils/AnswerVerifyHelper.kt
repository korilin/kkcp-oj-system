package com.korilin.utils

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

private const val ITEM_NOT_PASS = "用例没有通过"
private const val ITEMS_NOT_ALL_PASS = "没有通过所有用例"
private const val TIME_OUT_NOT_PASS = "执行超时，请检查代码的时间复杂度"
private const val UNKNOWN_ERROR = "代码执行发生无消息异常"
private const val PASS = "测试通过"
private fun elapsed(time: Int) = "代码耗时：${time}毫秒"

private const val INVOKE_FUN_NAME = "invoke"

object AnswerVerifyHelper {
    suspend fun verifyAnswer(clazz: Class<*>, data: Array<Map<String, Any>>, testMode: Boolean) =
        withContext(coroutineContext) {
            val invokeMethod = clazz.methods.find { it.name == INVOKE_FUN_NAME }!!
            val start = System.currentTimeMillis()
            var count = 0
            for (item in data) {
                val scope = CoroutineScope(Dispatchers.IO)
                try {
                    // 单个用例最多执行 2 秒
                    val result = withTimeout(2000) {
                        val job = scope.async {
                            invokeMethod.invoke(
                                null, item
                            ) as Boolean
                        }
                        job.await()
                    }
                    if (result) {
                        count++
                    } else {
                        val end = System.currentTimeMillis()
                        val time = (end - start).toInt()
                        if (testMode) {
                            return@withContext AnswerResult(false, "$item $ITEM_NOT_PASS", count, time)
                        }
                    }
                } catch (timeout: TimeoutCancellationException) {
                    val end = System.currentTimeMillis()
                    val time = (end - start).toInt()
                    return@withContext AnswerResult(false, TIME_OUT_NOT_PASS, count, time)
                } catch (e: Exception) {
                    val end = System.currentTimeMillis()
                    val time = (end - start).toInt()
                    return@withContext AnswerResult(false, e.message ?: UNKNOWN_ERROR, count, time)
                }
            }
            val end = System.currentTimeMillis()
            val time = (end - start).toInt()
            if (count == data.size) AnswerResult(true, "$PASS, ${elapsed(time)}", count, time)
            else AnswerResult(false, "$ITEMS_NOT_ALL_PASS, ${elapsed(time)}", count, time)
        }
}

data class AnswerResult(
    val status: Boolean, val message: String, val count: Int, val elapsed: Int
)