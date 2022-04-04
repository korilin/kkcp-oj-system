package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.ktorm.decodeJson
import com.korilin.table.AdminAccount
import com.korilin.utils.AESUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono


private val parser = PathPatternParser()

/**
 * 拦截规则列表
 */
private val patterns = arrayOf(
    parser.parse("${AdminModuleConfig.QUESTION_URL_PREFIX}/**"),
    parser.parse("${AdminModuleConfig.CONTEST_URL_PREFIX}/**")
)

@Component
class AdminFilter : WebFilter {
    var logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        var needFilter = false
        val request = exchange.request
        for (pattern in patterns) {
            needFilter = pattern.matches(request.path.pathWithinApplication())
            if (needFilter) break
        }
        if (needFilter) {
            val adminAccount = try {
                val token = request.headers["Admin-Token"]?.get(0) ?: throw NullPointerException("Admin Token is Null")
                val json = AESUtil.decrypt(AdminModuleConfig.ADMIN_ACCOUNT_AES_KEY, token)
                json.decodeJson<AdminAccount>()
            } catch (e: Exception) {
                val msg = "Admin Token Cast Failure: ${request.localAddress}/${request.remoteAddress} -> ${e.message}"
                logger.warn(msg)
                // 转换异常时返回 null
                null
            } ?: kotlin.run {
                // 当得不到 AdminAccount 对象时响应 401
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return Mono.empty() // <- return@filter
            }
            // TODO 以下行为待定
            // 校验 adminAccount 对象
            // 记录管理员的操作行为 -> 增删改
            println(adminAccount)
        }
        return chain.filter(exchange)
    }
}