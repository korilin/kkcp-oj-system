package com.korilin.controller

import com.korilin.IResponseBody
import com.korilin.UserModuleApiPrefix
import com.korilin.annotations.ExceptionMessageHandler
import com.korilin.annotations.RegisterExceptionMessage
import com.korilin.model.RegisterBody
import com.korilin.service.MainService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(UserModuleApiPrefix.BUSINESS_PREFIX)
class MainController(
    private val service: MainService
) {

    @PostMapping("/register")
    @ExceptionMessageHandler
    suspend fun register(@RequestBody body: RegisterBody): IResponseBody<Boolean> {
        return try {
            val (result, message) = service.register(body.contestId, body.userId)
            IResponseBody.success(result, message)
        } catch (npe: NullPointerException) {
            IResponseBody.error(npe.message!!)
        }
    }


}