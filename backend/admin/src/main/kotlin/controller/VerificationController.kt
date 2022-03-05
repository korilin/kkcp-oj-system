package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.model.AdminLoginModel
import com.korilin.model.LoginRequestBody
import com.korilin.service.VerificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(AdminModuleConfig.VERIFY_URL_PREFIX)
class VerificationController(
    private val verificationService: VerificationService
) {

    /**
     * 发送登录验证码到管理员邮箱
     * @param email 管理员邮箱
     */
    @PostMapping("/sendCode")
    @ExceptionMessageHandler
    suspend fun sendVerificationCode(@RequestParam("email") email: String): IResponseBody<Unit> {
        val result = verificationService.sendCodeToEmail(email)
        return if (result) IResponseBody.success("~ verification code send success ~")
        else IResponseBody.error("The admin account doesn't exit !")
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    @ExceptionMessageHandler
    suspend fun login(@RequestBody body: LoginRequestBody): IResponseBody<AdminLoginModel> {
        val result = verificationService.verifyLoginCode(body.email, body.code)
        if (!result) return IResponseBody.error("验证码错误或已过期")
        verificationService.doAdminLogin(body.email)?.let {
            return IResponseBody.success("登录成功", it)
        }
        return IResponseBody.error("登录失败，获取不到对应用户")
    }
}