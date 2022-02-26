package com.korilin.controller

import com.korilin.IResponseBody
import com.korilin.model.LoginRequestBody
import com.korilin.model.LoginResponseBody
import com.korilin.service.LoginService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class LoginController(
    private val loginService: LoginService
) {

    /**
     * 发送登录验证码到管理员邮箱
     * @param email 管理员邮箱
     */
    @PostMapping("/sendLoginCode")
    suspend fun sendVerificationCode(@RequestParam("email") email: String): IResponseBody<Unit> {
        val result = loginService.sendCodeToEmail(email)
        return if (result) IResponseBody.success("~ verification code send success ~")
        else IResponseBody.error("! verification code send failure !")
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    suspend fun login(body: LoginRequestBody): IResponseBody<LoginResponseBody> {
        val result = loginService.verifyLoginCode(body.email, body.code)
        if (!result) return IResponseBody.error("请重新发送验证码")
        loginService.doAdminLogin(body.email)?.let {
            return IResponseBody.success("登录成功", it)
        }
        return IResponseBody.error("登录失败，获取不到对应用户")
    }
}