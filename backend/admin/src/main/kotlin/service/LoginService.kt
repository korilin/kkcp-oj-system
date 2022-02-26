package com.korilin.service

import com.korilin.AdminModuleConfig
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextUInt

@Service
class LoginService(
    val redisTemplate: StringRedisTemplate
) {
    private val accountKey = AdminModuleConfig.ADMIN_ACCOUNT_AES_KEY

    /**
     * 将邮箱转化为存入 Redis 的 Key
     */
    private fun emailVerificationCodeKeyConvert(email: String): String = "EMAIL_VERIFICATION_CODE_KEY_$email"

    /**
     * 生成指定长度的验证码，
     * 数字和大写、小写字母各生成对应数量的 ASCII 编码，
     * 形成 3 个组合，再聚合起来随机获取形成验证码
     *
     * @param codeSize 验证码长度
     */
    private fun generateCode(codeSize: Int) : String {
        val array = arrayOf(48, 57, 65, 90, 97, 122)
        var index = 0
        val assemble = LinkedList<Int>().apply {
            repeat(3) {
                addAll(Array(codeSize) {
                    Random.nextInt(array[index] until array[index + 1])
                })
                index += 2
            }
        }
        return StringBuilder().apply {
            repeat(codeSize) {
                append(assemble.random().toChar())
            }
        }.toString()
    }

    /**
     * 发送验证码到管理员邮箱，并将验证码存储在 Redis 中提供后续校验，
     * 验证码在 Redis 中的有效期为 5 分钟。
     * - 当不存在管理员邮箱时返回 false
     * - 当存在管理员邮箱并且发送成功时返回 true
     * @param email 管理员邮箱
     * @return 是否发送成功
     */
    fun sendCodeToEmail(email: String): Boolean {
        // TODO 检查是否存在该邮箱
        val code = generateCode(6)
        val key = emailVerificationCodeKeyConvert(email)
        // 存入 redis 的验证码 5 分钟有效
        redisTemplate.opsForValue().set(key, code,5, TimeUnit.MINUTES)
        return true
    }
}