package com.korilin.kkcp.network

import com.korilin.kkcp.logError
import com.korilin.kkcp.storage.Store
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Exception

interface HttpService {

    @POST("/api/admin/verify/sendCode")
    suspend fun sendCode(@Query("email") email: String): ResponseBody<Unit>

    @POST("/api/admin/verify/login")
    suspend fun login(@Body body: LoginBody): ResponseBody<LoginResponse>

    @GET("/api/admin/manager/v5/account/all")
    suspend fun queryAllAccount(
        @Header("Admin-Token") token: String? = Store.token
    ): ResponseBody<Array<Account>>

    @GET("/api/admin/manager/v5/account/opts")
    suspend fun queryAllOption(
        @Header("Admin-Token") token: String? = Store.token
    ): ResponseBody<Array<Option>>

    @POST("/api/admin/manager/v5/account/new")
    suspend fun newAccount(
        @Header("Admin-Token") token: String? = Store.token,
        @Body body: Account
    ): ResponseBody<Boolean>

    @DELETE("/api/admin/manager/v5/account/del")
    suspend fun deleteAccount(
        @Header("Admin-Token") token: String? = Store.token,
        @Query("email") email: String
    ): ResponseBody<Boolean>

    @GET("/api/admin/manager/v5/user/all")
    suspend fun queryAllUser(@Header("Admin-Token") token: String? = Store.token): ResponseBody<Array<User>>

    @PUT("/api/admin/manager/v5/user/block")
    suspend fun blockUser(
        @Header("Admin-Token") token: String? = Store.token,
        @Query("userId") userId: Int,
        @Query("status") status: Boolean
    ): ResponseBody<Boolean>

}

private const val IP = "10.0.2.2"
private const val URL = " http://${IP}:2021"

private val retrofit = Retrofit.Builder()
    .baseUrl(URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val httpService: HttpService = retrofit.create(HttpService::class.java)

suspend fun <T> CoroutineScope.request(
    call: suspend () -> ResponseBody<T>,
    success: suspend (T?) -> Unit
) {
    try {
        val body = call()
        if (body.status) {
            success(body.data)
        } else {
            logError(body.message)
        }
    } catch (e: Exception) {
        logError(e.stackTraceToString())
    }
}
