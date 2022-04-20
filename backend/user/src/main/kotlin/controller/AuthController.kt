package com.korilin.controller

import com.korilin.urls.FrontendUrl
import com.korilin.IResponseBody
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.service.AuthService
import com.korilin.domain.table.UserProfile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.result.view.Rendering
import java.util.*

@Controller
class AuthController(
    private val authService: AuthService
) {

    /**
     * 生成客户端唯一ID
     */
    @GetMapping("/auth/random")
    @ResponseBody
    @ExceptionMessageHandler
    suspend fun authRandom(): IResponseBody<String> {
        val uuid = UUID.randomUUID().toString()
        return IResponseBody.success(data = uuid)
    }

    /**
     * GitHub 用户授权回调，校验 state，使用 code 获取用户 token，
     * 重定向到主页面
     * @param code 授权码
     * @param state 应当为用户使用 [authRandom] 生成的 uid
     */
    @GetMapping("/auth/callback")
    suspend fun authCallback(
        @RequestParam code: String, @RequestParam state: String
    ): Rendering {
        val body = authService.requestUserToken(code)
        if (body.accessToken != null) {
            authService.saveUserToken(state, body.accessToken!!)
            return Rendering.redirectTo(FrontendUrl.HOME_PAGE).build()
        }
        return Rendering.redirectTo("${FrontendUrl.ERROR_PAGE}?info=授权失败").build()
    }

    @PostMapping("/auth/token")
    @ResponseBody
    @ExceptionMessageHandler
    suspend fun getAuthToken(uid: String): IResponseBody<String> {
        val token = authService.getUserToken(uid)
        return IResponseBody.success(token)
    }

    @PutMapping("/user/update")
    @ResponseBody
    @ExceptionMessageHandler
    suspend fun updateUserProfile(@RequestBody profile: UserProfile): IResponseBody<Boolean> {

        return IResponseBody.success(authService.updateProfile(profile))
    }
}