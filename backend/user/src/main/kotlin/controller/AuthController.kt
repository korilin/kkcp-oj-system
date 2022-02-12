package com.korilin.controller

import IResponseBody
import com.korilin.service.AuthService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.reactive.result.view.Rendering
import java.util.*

@Controller
class AuthController(
    val authService: AuthService
) {

    /**
     * 生成客户端唯一ID
     */
    @GetMapping("/auth/random")
    @ResponseBody
    suspend fun authRandom(): IResponseBody<String> {
        val uuid = UUID.randomUUID().toString()
        return IResponseBody.success(data = uuid)
    }

    /**
     * GitHub 用户授权回调，校验 state，使用 code 获取用户 token，
     * 重定向到主页面
     */
    @GetMapping("/auth/callback")
    suspend fun authCallback(
        @RequestParam code: String, @RequestParam state: String
    ): Rendering {
        // TODO state 校验
        val body = authService.requestUserToken(code)
        if (body.accessToken != null) {
            return Rendering.redirectTo(FrontendUrl.HOME_PAGE).build()
        }
        return Rendering.redirectTo("${FrontendUrl.ERROR_PAGE}?info=授权失败").build()
    }
}