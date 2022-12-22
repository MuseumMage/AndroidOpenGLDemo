package com.example.androidlearnopengl

import android.content.Context
import java.io.*


class Utils {
    companion object {
        fun readFile(filename: String, context: Context): String {
            var inputStream: InputStream? = null
            var reader: Reader? = null
            var bufferedReader: BufferedReader? = null
            try {
                inputStream = context.resources.assets.open(filename)
                reader = InputStreamReader(inputStream) // 字符流
                bufferedReader = BufferedReader(reader) //缓冲流
                val result = StringBuilder()
                var temp: String?
                while (bufferedReader.readLine().also { temp = it } != null) {
                    result.append(temp)
                }
                return result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return String()
        }
    }
}