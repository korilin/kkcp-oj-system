package com.korilin

import com.korilin.utils.AESUtil

internal object AdminModuleConfig {
    internal val ADMIN_ACCOUNT_AES_KEY = AESUtil.createKey()
    internal val VERIFICATION_CODE_SIZE = 6

    internal const val ADMIN_URL_PREFIX = "/admin"

    // 登录验证接口前缀
    internal const val VERIFY_URL_PREFIX = "$ADMIN_URL_PREFIX/verify"

    // 问题接口前缀
    internal const val QUESTION_URL_PREFIX = "$ADMIN_URL_PREFIX/question"

    // 竞赛接口前缀
    internal const val CONTEST_URL_PREFIX = "$ADMIN_URL_PREFIX/contest"
}
