package com.korilin.annotations

import com.korilin.ANNOTATION_PACKAGE
import com.korilin.IResponseBody
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.jline.utils.Log
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * 使用该注解标注的 API 函数出现异常时将会被 [ExceptionMessageHandlerAspect.around] 处理，
 * 为了限制只有 API 函数能使用该注解，返回值不为 [IResponseBody] 的函数使用该注解时将会报错，
 * 可以使用 @RegisterExceptionMessage 注解来注册指定异常的返回信息，
 * 如果没有匹配的异常规则将会提示 "There have an unregistered exception"。
 *
 * @see RegisterExceptionMessage
 * @see ExceptionMessageHandlerAspect
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExceptionMessageHandler

/**
 * 向 [ExceptionMessageHandler] 标注的 RequestMapping 注册一个异常信息规则，
 * 该注解必须和 @HandleControllerException 注解一起使用
 * 匹配时通过 [KClass.isSuperclassOf] 来判定是否能够匹配异常类型，
 * 对于异常的基类 [Exception] 将会匹配所有异常，
 * 因此请将 KClass<Exception> 规则放在最后注册。
 */
@MustBeDocumented
@Repeatable
annotation class RegisterExceptionMessage(val exceptionKClass: KClass<out Exception>, val message: String)

private val castExceptionMessage =
    "The return value of the @${ExceptionMessageHandler::class.java.simpleName} annotated method must be " + IResponseBody::class.java.name

private fun unregisteredExceptionMessage(e: Exception) =
    "There have an unregistered exception: ${e::class.java} -> ${e.message}"

const val USER_EXCEPTION_MESSAGE = "USER_EXCEPTION_MESSAGE"

/**
 * 异常响应信息处理切面
 *
 * @see ExceptionMessageHandler
 * @see RegisterExceptionMessage
 */
@Aspect
@Component
class ExceptionMessageHandlerAspect {

    @Pointcut("@annotation(${ANNOTATION_PACKAGE}.ExceptionMessageHandler)")
    fun exceptionMessageHandlerPointcut() {
    }

    @Around("exceptionMessageHandlerPointcut()")
    @Suppress("UNCHECKED_CAST")
    fun around(joinPoint: ProceedingJoinPoint): Any {
        return try {
            return joinPoint.proceed()
        } catch (e: Exception) {
            val method = (joinPoint.signature as MethodSignature).method
            val annotations = method.getAnnotationsByType(RegisterExceptionMessage::class.java)
            var message: String? = null
            for (annotation in annotations) {
                val exceptionKClass = annotation.exceptionKClass
                if (exceptionKClass.isSuperclassOf(e::class)) {
                    message = annotation.message
                    if (message == USER_EXCEPTION_MESSAGE) {
                        message = e.message
                    }
                    break
                }
            }
            IResponseBody.error<Unit>(message ?: let {
                Log.trace("Error Happen at ${LocalDateTime.now()}")
                e.printStackTrace()
                unregisteredExceptionMessage(e)
            })
        }
    }
}
