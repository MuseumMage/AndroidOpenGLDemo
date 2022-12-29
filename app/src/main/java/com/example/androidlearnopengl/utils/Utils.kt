package com.example.androidlearnopengl.utils

import android.opengl.GLES20
import com.example.androidlearnopengl.OpenGLApplication.Companion.context
import java.io.*


class Utils {
    companion object {
        fun readFile(resid: Int): String {
            var inputStream: InputStream? = null
            var reader: Reader? = null
            var bufferedReader: BufferedReader? = null
            try {
//                inputStream = context.resources.assets.open(filename)
                inputStream = context.resources.openRawResource(resid)
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

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

    }
}