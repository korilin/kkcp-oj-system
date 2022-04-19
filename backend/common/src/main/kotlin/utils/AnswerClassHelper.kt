package com.korilin.utils

import com.korilin.CompileFailureException
import java.io.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

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

        fun getClassName(baseName: String): String {
            // 编译后的 Class Name 附带 Kt
            // Answer + ? + Kt
            return "$FILE_PREFIX$baseName$CLASS_SUFFIX"
        }

        /**
         * @param name userId
         */
        override fun findClass(name: String): Class<*> {
            // Answer[uid]Offset[offset]Kt
            val className = getClassName(name)
            val classBytes = loadClassFile(className)
            return defineClass(className, classBytes, 0, classBytes.size)
        }

        private fun loadClassFile(className: String): ByteArray {
            var inputStream: FileInputStream? = null
            var outputStream: ByteArrayOutputStream? = null
            return try {
                // className + .class
                // Answer[uid]Offset[offset]Kt.class
                val file = File("$classPath/$className$CLASS_FILE_TYPE")
                inputStream = FileInputStream(file)
                outputStream = ByteArrayOutputStream()
                var ch = inputStream.read()
                while (ch != -1) {
                    outputStream.write(ch)
                    ch = inputStream.read()
                }
                file.deleteOnExit()
                outputStream.toByteArray()
            } catch (ioe: IOException) {
                throw ClassNotFoundException("Class $name could not be found")
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
    }

    private val classLoaders = ConcurrentHashMap<Int, QuestionClassLoader>()

    private fun getFullPath(questionId: Int): String {
        return "$PATH/$DIR_PREFIX$questionId"
    }

    private fun getKotlinFileName(fileName: String): String {
        // Answer + ? + .kt
        return "$FILE_PREFIX$fileName$KT_FILE_TYPE"
    }

    private fun getClassPath(questionId: Int): String {
        return "${getFullPath(questionId)}/$CLASS_DIR_NAME"
    }

    private fun createDir(dirPath: String) {
        val dir = File(dirPath)
        dir.mkdirs()
    }

    private fun saveKotlinFile(path: String, code: String): File {
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(code)
        return file
    }

    private fun compileKtToClass(filePath: String, targetPath: String) {
        val process = Runtime.getRuntime().exec("$KOTLINC_CMD $filePath -d $targetPath")
        val info = process.errorStream.text
        if (info.isNotBlank() || info.isNotEmpty()) {
            val message =  Regex(filePath).replace(info, "")
            throw CompileFailureException(message)
        }
    }

    private val counter = AtomicInteger()

    fun createClass(questionId: Int, userId: Int, code: String): Class<*> {
        val offset: Int = counter.getAndIncrement()
        // [uid]Offset[offset]
        val className = "${userId}Offset$offset"
        val filePath = getFullPath(questionId)
        // Answer[uid]Offset[offset]
        val fileName = getKotlinFileName(className)
        val ktAbstractPath = "$filePath/$fileName"
        val classAbstractPath = getClassPath(questionId)
        createDir(filePath)
        val file = saveKotlinFile(ktAbstractPath, code)
        compileKtToClass(ktAbstractPath, classAbstractPath)
        val loader = classLoaders.getOrPut(questionId) {
            QuestionClassLoader(classAbstractPath)
        }
        file.deleteOnExit()
        return loader!!.loadClass(className)
    }

    fun removeClassLoader(questionId: Int) {
        classLoaders[questionId]?.let {
            classLoaders.remove(questionId, it)
        }
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