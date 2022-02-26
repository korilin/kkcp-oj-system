package com.korilin.github.serializable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    @SerialName("access_token") var accessToken: String? = null,
    @SerialName("scope") var scope: String? = null,
    @SerialName("token_type") var tokenType: String? = null,
    @SerialName("error") var error: String? = null,
    @SerialName("error_description") var errorDescription: String? = null
) {
    override fun toString() =
        "access_token=$accessToken&scope=$scope&token_type=$tokenType&error=$error&error_description=$errorDescription"
}