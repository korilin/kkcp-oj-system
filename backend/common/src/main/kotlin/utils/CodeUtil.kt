package com.korilin.utils

object CodeUtil {

    private val GET_REGEX = Regex("""(?<=// user start)([\s\S]*)(?=// user end)""")
    private val REPLACE_REGEX = Regex("""// user start([\s\S]*)// user end""")

    fun getUserCodeTemplate(codeTemplate: String): String {
        val result = GET_REGEX.find(codeTemplate)
        println(codeTemplate)
        println(result?.value)
        return result?.value ?: ""
    }

    fun compositeAnswerCode(codeTemplate: String, answer: String): String {
        return "${answer}\n// answer end\n${REPLACE_REGEX.replace(codeTemplate, "")}"
    }
}