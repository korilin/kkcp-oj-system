package com.korilin.service

import com.korilin.github.GitHubAuth
import com.korilin.github.serializable.AccessTokenResponse
import com.korilin.domain.table.UserProfile
import com.korilin.domain.userProfiles
import com.korilin.urls.GitHubApis
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AuthService(
    private val redisTemplate: StringRedisTemplate,
    database: Database
) {

    private val userProfiles = database.userProfiles

    companion object {
        private const val USER_TOKEN_KEY_PREFIX = "USER_TOKEN_KEY_PREFIX_"
    }

    private val client: HttpClient = HttpClient(CIO) {
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

    internal suspend fun saveUserToken(uid: String, token: String) {
        val key = "$USER_TOKEN_KEY_PREFIX$uid"
        redisTemplate.opsForValue().set(key, token, 5, TimeUnit.MINUTES)
    }

    internal suspend fun getUserToken(uid: String): String? {
        val key = "$USER_TOKEN_KEY_PREFIX$uid"
        return redisTemplate.opsForValue().get(key)
    }

    internal suspend fun updateProfile(profile: UserProfile): Boolean {
        val r = userProfiles.find { it.id eq profile.id }?.let {
            it.name = profile.name
            it.login = profile.login
            it.email = profile.email
            it.avatarUrl = profile.avatarUrl
            it.bio = profile.bio
            it.htmlUrl = profile.htmlUrl
            it.flushChanges()
        } ?: userProfiles.add(profile)
        return r == 1
    }
}