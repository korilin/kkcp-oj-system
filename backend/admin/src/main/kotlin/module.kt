package com.korilin

import com.korilin.utils.AESUtil

internal object AdminModuleConfig {
    internal val ADMIN_ACCOUNT_AES_KEY = AESUtil.createKey()
}
