package com.korilin
data class IResponseBody<T>(
    val status: Boolean, val message: String, val data: T?
) {
    companion object {
        fun <T> success(message: String = "", data: T? = null) = IResponseBody(true, message, data)
        fun <T> error(message: String = "", data: T? = null) = IResponseBody(false, message, data)
    }
}