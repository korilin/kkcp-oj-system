package utils

import java.security.Key
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

/**
 * AES 对称加密工具单例
 */
object AESUtil {

    /**
     * 创建一个对称加密密钥，随机生成一个 UUID 来作为 seed，
     * 调用 [createKey(seed: String)] 方法来生成一个 Key
     */
    fun createKey(): Key = createKey(UUID.randomUUID().toString())

    /**
     * 通过提供的 seed 来建一个对称加密密钥
     */
    fun createKey(seed: String): Key = KeyGenerator.getInstance("AES").apply {
        init(128, SecureRandom(seed.toByteArray()))
    }.generateKey()

    /**
     * AES 对称加密 - 加密操作
     * @see aesDecode
     */
    fun aesEncode(key: Key, input: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val outputByte = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(outputByte)
    }

    /**
     * AES 对称加密 - 解密操作
     * @see aesEncode
     */
    fun aesDecode(key: Key, input: String): String {
        val decode = Base64.getDecoder().decode(input)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputByte = cipher.doFinal(decode)
        return String(outputByte)
    }
}