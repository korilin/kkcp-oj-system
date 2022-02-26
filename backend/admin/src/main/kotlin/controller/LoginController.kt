package com.korilin.controller

import IResponseBody
import com.korilin.service.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class LoginController(
    private val loginService: LoginService
){

    /**
     * 发送登录验证码到管理员邮箱
     * @param email 管理员邮箱
     */
    @PostMapping("/sendLoginCode")
    suspend fun sendVerificationCode(@RequestParam("email") email: String): IResponseBody<Unit> {
        val result =  loginService.sendCodeToEmail(email)
        return if(result)
            IResponseBody.success("~ verification code send success ~")
        else IResponseBody.error("! verification code send failure !")
    }

}