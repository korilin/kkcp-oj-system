package com.korilin.utils


object AnswerVerifyHelper {
    fun verifyAnswer(clazz: Class<*>, data: Array<Map<String, Any>>): Pair<Boolean, String> {
        val instance = clazz.getConstructor().newInstance()
        val invokeMethod = clazz.methods.find { it.name == "invoke" }!!
        for (item in data) {
            if (!(invokeMethod.invoke(instance, data) as Boolean)) {
                return Pair(false, "$item 用例没有通过")
            }
        }
        return Pair(true, "")
    }
}