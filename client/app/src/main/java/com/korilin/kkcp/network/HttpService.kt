package com.korilin.kkcp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface HttpService {

    @POST("/api/admin/verify/sendCode")
    suspend fun sendCode(): ResponseBody<Unit>

    @POST("/api/admin/verify/login")
    suspend fun login(@Body body: LoginBody): ResponseBody<LoginResponse>

}

private const val IP = "10.0.2.2"
private const val URL = " http://${IP}:2021"

private val retrofit = Retrofit. Builder()
    .baseUrl(URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val httpService: HttpService = retrofit.create(HttpService::class.java)
