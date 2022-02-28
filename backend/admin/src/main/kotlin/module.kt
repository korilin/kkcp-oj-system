package com.korilin

import com.korilin.utils.AESUtil

internal object AdminModuleConfig {
    internal val ADMIN_ACCOUNT_AES_KEY = AESUtil.createKey()

    internal const val ADMIN_URL_PREFIX = "/admin"

    // 登录验证接口前缀
    internal const val VERIFY_URL_PREFIX = "$ADMIN_URL_PREFIX/verify"

    // 查询接口前缀
    internal const val QUERY_URL_PREFIX = "$ADMIN_URL_PREFIX/query"

    // 操作接口前缀
    internal const val OPT_URL_PREFIX = "$ADMIN_URL_PREFIX/opt"

    // 问题操作接口前缀
    internal const val QUESTION_OPS_URL_PREFIX = "$OPT_URL_PREFIX/question"

    // 竞赛操作接口前缀
    internal const val CONTEST_OPS_URL_PREFIX = "$OPT_URL_PREFIX/contest"
}
