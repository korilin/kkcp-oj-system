package com.korilin.service

import com.korilin.github.GitHubAuth
import com.korilin.github.serializable.AccessTokenResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.stereotype.Service

@Service
class AuthService {


    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    internal suspend fun requestUserToken(code: String): AccessTokenResponse {
        val uri = GitHubApis.AUTH_ACCESS_TOKEN
        val response: HttpResponse = client.request(uri) {
            method = HttpMethod.Post
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            parameter("client_id", GitHubAuth.clientId)
            parameter("code", code)
            parameter("client_secret", GitHubAuth.clientSecret)
            parameter("redirect_uri", GitHubAuth.redirectUri)
        }
        return response.receive();
    }

}