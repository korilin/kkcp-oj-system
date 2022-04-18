package com.korilin.utils

import com.korilin.CompileFailureException
import java.io.*
import java.util.concurrent.ConcurrentHashMap

private const val PATH = "/Users/linjiebin/Documents/ktfiles"
private const val CLASS_DIR_NAME = "classes"
private const val KOTLINC_CMD = "kotlinc"

object AnswerClassHelper {
    private const val DIR_PREFIX = "Question"
    private const val FILE_PREFIX = "Answer"
    private const val CLASS_SUFFIX = "Kt"
    private const val CLASS_FILE_TYPE = ".class"
    private const val KT_FILE_TYPE = ".kt"

    private class QuestionClassLoader(val classPath: String) : ClassLoader() {

        fun getClassName(userId: String): String {
            return "$FILE_PREFIX$userId$CLASS_SUFFIX"
        }

        /**
         * @param name userId
         */
        override fun findClass(name: String): Class<*> {
            val className = getClassName(name)
            val classBytes = loadClassFile(className)
            return defineClass(className, classBytes, 0, classBytes.size)
        }

        private fun loadClassFile(className: String): ByteArray {
            var inputStream: FileInputStream? = null
            var outputStream: ByteArrayOutputStream? = null
            return try {
                val file = File("$classPath/$className$CLASS_FILE_TYPE")
                inputStream = FileInputStream(file)
                outputStream = ByteArrayOutputStream()
                var ch = inputStream.read()
                while (ch != -1) {
                    outputStream.write(ch)
                    ch = inputStream.read()
                }
                outputStream.toByteArray()
            } catch (ioe: IOException) {
                throw ClassNotFoundException("Class $name could not be found")
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
    }

    private fun getFullPath(questionId: Int): String {
        return "$PATH/$DIR_PREFIX$questionId"
    }

    private fun getKotlinFileName(userId: String): String {
        return "$FILE_PREFIX$userId$KT_FILE_TYPE"
    }

    private fun getClassPath(questionId: Int): String {
        return "${getFullPath(questionId)}/$CLASS_DIR_NAME"
    }

    private fun createDir(dirPath: String) {
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdir()
        }
    }

    private fun saveKotlinFile(path: String, code: String) {
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(code)
    }

    private fun compileKtToClass(filePath: String, targetPath: String) {
        val process = Runtime.getRuntime().exec("$KOTLINC_CMD $filePath -d $targetPath")
        val info = process.errorStream.text
        if (info.isNotBlank() || info.isNotEmpty()) throw CompileFailureException(info)
    }

    fun createClass(questionId: Int, userId: Int, code: String): Class<*> {
        val filePath = getFullPath(questionId)
        val fileName = getKotlinFileName(userId.toString())
        val ktAbstractPath = "$filePath/$fileName"
        val classAbstractPath = getClassPath(questionId)
        createDir(filePath)
        saveKotlinFile(ktAbstractPath, code)
        compileKtToClass(ktAbstractPath, classAbstractPath)
        val loader = QuestionClassLoader(classAbstractPath)
        return loader.loadClass(userId.toString())
    }

    private val InputStream.text: String
        get() {
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