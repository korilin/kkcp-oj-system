package com.korilin.controller

import com.korilin.CompileFailureException
import com.korilin.IResponseBody
import com.korilin.UserModuleApiPrefix
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.annotations.RegisterExceptionMessage
import com.korilin.annotations.USER_EXCEPTION_MESSAGE
import com.korilin.domain.table.SubmitRecord
import com.korilin.model.*
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
                    handlingUser.remove(userId, beforeTask)
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

    @GetMapping("/query/submits")
    @ExceptionMessageHandler
    suspend fun getSubmits(questionId: Int, userId: Int): IResponseBody<List<SubmitRecord>> {
        val list = service.getSubmits(userId, questionId)
        return IResponseBody.success(data = list)
    }

    @PutMapping("/answer/update")
    @ExceptionMessageHandler
    suspend fun updateAnswer(@RequestBody body: AnswersUpdateBody): IResponseBody<Unit> {
        val result = service.updateAnswer(body.userId, body.answers)
        return IResponseBody(result, "", Unit)
    }

    private suspend fun answerCatch(
        body: AnswersUpdateBody,
        block: suspend (QuestionAnswer) -> IResponseBody<Boolean>
    ) = try {
        // 只取第一个问题进行测试
        val answer = body.answers.getOrNull(0) ?: throw IndexOutOfBoundsException("获取不到答案")
        service.updateAnswer(body.userId, body.answers)
        block(answer)
    } catch (e: IndexOutOfBoundsException) {
        IResponseBody.success(message = e.message ?: "获取不到答案", data = false)
    } catch (e: ClassNotFoundException) {
        IResponseBody.success(message = e.message ?: "找不到对应类，可能存在编译失败", data = false)
    } catch (e: NoSuchMethodException) {
        IResponseBody.success(message = e.message ?: "找不到验证方法", data = false)
    } catch (e: Exception) {
        IResponseBody.success(message = e.message ?: e.stackTrace.contentToString(), data = false)
    }

    @PostMapping("/answer/test")
    suspend fun testAnswer(@RequestBody body: AnswersUpdateBody): IResponseBody<Boolean> = serialOpt(body.userId) {
        answerCatch(body) { answer ->
            val (result, message) = service.testAnswer(body.userId, answer.questionId, answer.answer)
            IResponseBody.success(message = message, data = result)
        }
    }

    @PostMapping("/answer/submit")
    suspend fun submitAnswer(@RequestBody body: AnswersUpdateBody): IResponseBody<Boolean> = serialOpt(body.userId) {
        answerCatch(body) { answer ->
            val (result, message) = service.submitAnswer(body.userId, answer.questionId, answer.answer)
            IResponseBody.success(message = message, data = result)
        }
    }

    @GetMapping("/query/me/contests")
    suspend fun getMyContests(userId: Int): IResponseBody<Array<MyRegister>> {
        val data = service.getMyContests(userId)
        return IResponseBody.success(data = data.toTypedArray())
    }
}