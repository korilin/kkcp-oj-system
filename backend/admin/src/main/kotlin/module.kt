package com.korilin

import utils.AESUtil

internal object AdminModuleConfig {
    internal val ADMIN_ACCOUNT_AES_KEY = AESUtil.createKey()
}
