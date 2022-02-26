package com.korilin.annotations

import com.korilin.ANNOTATION_PACKAGE
import com.korilin.IResponseBody
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * 使用该注解标注的 API 函数出现异常时将会被 [HandleExceptionMessageAspect.around] 处理，
 * 为了限制只有 API 函数能使用该注解，返回值不为 [IResponseBody] 的函数使用该注解时将会报错，
 * 可以使用 @RegisterExceptionMessage 注解来注册指定异常的返回信息，
 * 如果没有匹配的异常规则将会提示 "There have an unregistered exception"。
 *
 * @see RegisterExceptionMessage
 * @see HandleExceptionMessageAspect
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
 *
 * @see HandleExceptionMessageAspect.around
 */
@MustBeDocumented
@Repeatable
annotation class RegisterExceptionMessage(val exceptionKClass: KClass<out Exception>, val message: String)

/**
 * 异常响应信息处理切面
 *
 * @see ExceptionMessageHandler
 * @see RegisterExceptionMessage
 */
@Aspect
@Component
public class HandleExceptionMessageAspect {

    @Pointcut("@annotation(${ANNOTATION_PACKAGE}.ExceptionMessageHandler)")
    public fun handleControllerExceptionPointCut() {
    }

    private val typeCastExceptionMessage =
        "The return value of the @${ExceptionMessageHandler::class.java.simpleName} annotated method must be " +
                IResponseBody::class.java.name

    private fun unregisteredExceptionMessage(e: Exception) =
        "There have an unregistered exception: ${e::class.java} -> ${e.message}"

    @Around("handleControllerExceptionPointCut()")
    @Suppress("UNCHECKED_CAST")
    fun around(joinPoint: ProceedingJoinPoint): IResponseBody<Unit> {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        if (method.returnType != IResponseBody::class.java) throw TypeCastException(
            typeCastExceptionMessage
        )
        return try {
            joinPoint.proceed() as IResponseBody<Unit>
        } catch (e: Exception) {
            val annotations = method.getAnnotationsByType(RegisterExceptionMessage::class.java)
            for (annotation in annotations) {
                val exceptionKClass = annotation.exceptionKClass
                if (exceptionKClass.isSuperclassOf(e::class)) {
                    return IResponseBody.error(annotation.message)
                }
            }
            val message = unregisteredExceptionMessage(e)
            e.printStackTrace()
            return IResponseBody.error(message)
        }
    }
}