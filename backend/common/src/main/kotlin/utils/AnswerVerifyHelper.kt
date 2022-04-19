package com.korilin.utils

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


object AnswerVerifyHelper {
    suspend fun verifyAnswer(clazz: Class<*>, data: Array<Map<String, Any>>) = withContext(coroutineContext) {
        val invokeMethod = clazz.methods.find { it.name == "invoke" }!!
        val start = System.currentTimeMillis()
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
                if (!result) {
                    return@withContext Pair(false, "$item 用例没有通过")
                }
            } catch (timeout: TimeoutCancellationException) {
                return@withContext Pair(false, "执行超时，请检查代码的时间复杂度")
            } catch (e: Exception) {
                return@withContext Pair(false, e.message ?: "代码执行发生无消息异常")
            }
        }
        val end = System.currentTimeMillis()
        return@withContext Pair(true, "测试通过，代码耗时：${end - start}毫秒")
    }
}