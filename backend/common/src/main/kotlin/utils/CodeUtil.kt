package com.korilin.utils

object CodeUtil {
    private const val CODE_REGEX = """(?<=// user start)([\s\S]*)(?=// user end)"""

    fun getUserCodeTemplate(codeTemplate: String): String {
        val regex = Regex(CODE_REGEX)
        val result = regex.find(codeTemplate)
        println(codeTemplate)
        println(result?.value)
        return result?.value ?: ""
    }
}