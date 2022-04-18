package com.korilin.utils

import com.korilin.CompileFailureException
import java.io.*
import java.util.concurrent.ConcurrentHashMap

private const val PATH = "/Users/linjiebin/Documents/ktfiles"
private const val CLASS_DIR_NAME = "classes"
private const val KOTLINC_CMD = "kotlinc"

object QuestionCodeHelper {
    private const val DIR_PREFIX = "Question"
    private const val FILE_PREFIX = "Answer"
    private const val CLASS_FILE_TYPE = "Kt.class"
    private const val KT_FILE_TYPE = ".kt"


    private fun getFullPath(questionId: Int): String {
        return "$PATH/$DIR_PREFIX$questionId"
    }

    private fun getKotlinFileName(userId: String) : String {
        return "$FILE_PREFIX$userId$KT_FILE_TYPE"
    }

    private fun getClassPath(questionId: Int): String {
        return "${getFullPath(questionId)}/$CLASS_DIR_NAME"
    }

    private fun saveKotlinFile(path: String, code: String) {
        val file =  File(path)
        file.writeText(code)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    private fun compileKtToClass(filePath: String, targetPath: String) {
        val process = Runtime.getRuntime().exec("$KOTLINC_CMD $filePath -d $targetPath")
        val info = process.errorStream.text
        if (info.isNotBlank() || info.isNotEmpty()) throw CompileFailureException(info)
    }


    private val InputStream.text: String get() {
        var output = ""
        val isr = InputStreamReader(this)
        val reader = BufferedReader(isr)
        var line: String? = reader.readLine()
        while (line != null) {
            output += line + "\n"
            line = reader.readLine()
        }
        return output
    }

    
}