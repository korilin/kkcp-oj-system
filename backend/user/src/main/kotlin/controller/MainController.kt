package com.korilin.controller

import com.korilin.IResponseBody
import com.korilin.UserModuleApiPrefix
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.model.AnswerUpdateBody
import com.korilin.model.UnderWayContestUserData
import com.korilin.model.RegisterBody
import com.korilin.service.MainService
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping(UserModuleApiPrefix.BUSINESS_PREFIX)
class MainController(
    private val service: MainService
) {

    @PostMapping("/register")
    @ExceptionMessageHandler
    suspend fun register(@RequestBody body: RegisterBody): IResponseBody<Boolean> {
        return try {
            val (result, message) = service.register(body.contestId, body.userId)
            IResponseBody.success(result, message)
        } catch (npe: NullPointerException) {
            IResponseBody.error(npe.message!!)
        }
    }

    private val handlingUser = ConcurrentHashMap<Int, Deferred<*>>()

    /**
     * 用户串行操作，保证同一时间同一用户只有一个操作在完成，但不保证操作的执行顺序
     */
    private suspend fun <T> serialOpt(userId: Int, block: suspend () -> T): T {
        return withContext(Dispatchers.Default) {
            val task = async(start = CoroutineStart.LAZY) { block() }
            do {
                val beforeTask = registerTaskIfNullAsync(userId, task)
                try {
                    beforeTask?.await()
                } catch (_: Exception) {
                    // continue
                }
            } while (beforeTask != null)
            task.start()
            val result = task.await()
            handlingUser.remove(userId, task)
            result
        }
    }

    private fun registerTaskIfNullAsync(userId: Int, task: Deferred<*>): Deferred<*>? {
        synchronized(handlingUser) {
            val value = handlingUser[userId]
            if (value == null) {
                handlingUser[userId] = task
            }
            return value
        }
    }

    /**
     * - 用户没有报名时无法参加
     * - 获取活动信息和题目信息
     * - 获取用户题目答案信息
     */
    @GetMapping("/query/contest")
    @ExceptionMessageHandler
    suspend fun getUnderWayData(userId: Int): IResponseBody<UnderWayContestUserData> = serialOpt(userId) {
        val (message, registration) = service.getUserRegisterUnderWayContest(userId)
        if (registration == null) return@serialOpt IResponseBody.error(message)
        val questionAndAnswer = service.getQuestionsWithAnswer(registration)
        IResponseBody.success(
            data = UnderWayContestUserData(
                registration.contest,
                questionAndAnswer
            )
        )
    }

    @GetMapping("/query/commits")
    @ExceptionMessageHandler
    suspend fun getCommits(questionId: Int, userId: Int): IResponseBody<Unit> {
        TODO()
    }

    @PutMapping("/answer/update")
    @ExceptionMessageHandler
    suspend fun updateAnswer(@RequestBody body: AnswerUpdateBody) : IResponseBody<Unit> {
        val result = service.updateAnswer(body.userId, body.answers)
        return IResponseBody(result, "", Unit)
    }
}