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

    private class QuestionClassLoader(val classPath: String) : ClassLoader() {

        fun getClassName(userId: String): String {
            return "$FILE_PREFIX$userId$CLASS_FILE_TYPE"
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
                val file = File("$classPath/$className")
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

    private val classLoaders = ConcurrentHashMap<Int, QuestionClassLoader>()

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

    fun createClass(questionId: Int, userId: Int, code: String): Class<*> {
        val filePath = getFullPath(questionId)
        val fileName= getKotlinFileName(userId.toString())
        val ktAbstractPath = "$filePath/$fileName"
        val classAbstractPath = getClassPath(questionId)
        saveKotlinFile(ktAbstractPath, code)
        compileKtToClass(ktAbstractPath, classAbstractPath)
        var loader = classLoaders[questionId]
        if (loader == null) {
            synchronized(classLoaders) {
                if (classLoaders[questionId] == null) {
                    loader = QuestionClassLoader(classAbstractPath)
                    classLoaders[questionId] = loader!!
                }
            }
        }
        return loader!!.loadClass(userId.toString())
    }

    fun removeClassLoader(questionId: Int) {
        classLoaders[questionId]?.let {
            classLoaders.remove(questionId, it)
        }
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