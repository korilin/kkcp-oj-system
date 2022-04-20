package com.korilin.utils

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


object AnswerVerifyHelper {
    suspend fun verifyAnswer(clazz: Class<*>, data: Array<Map<String, Any>>, testMode: Boolean) =
        withContext(coroutineContext) {
            val invokeMethod = clazz.methods.find { it.name == "invoke" }!!
            val start = System.currentTimeMillis()
            var count = 0
            for (item in data) {
                val scope = CoroutineScope(Dispatchers.IO)
                try {
                    // 单个用例最多执行 3 秒
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
                        val message = if (testMode) "$item 用例没有通过" else "没有通过所有用例"
                        return@withContext AnswerResult(false, message, count, time)
                    }
                } catch (timeout: TimeoutCancellationException) {
                    val end = System.currentTimeMillis()
                    val time = (end - start).toInt()
                    return@withContext AnswerResult(false, "执行超时，请检查代码的时间复杂度", count, time)
                } catch (e: Exception) {
                    val end = System.currentTimeMillis()
                    val time = (end - start).toInt()
                    return@withContext AnswerResult(false, e.message ?: "代码执行发生无消息异常", count, time)
                }
            }
            val end = System.currentTimeMillis()
            val time = (end - start).toInt()
            AnswerResult(true, "测试通过，代码耗时：${time}毫秒", count, time)
        }
}

data class AnswerResult(
    val status: Boolean, val message: String, val count: Int, val elapsed: Int
)