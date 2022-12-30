package com.example.androidlearnopengl.utils

import android.content.Context
import android.content.res.Resources
import android.opengl.GLES30
import android.util.Log
import androidx.annotation.RawRes
import com.example.androidlearnopengl.OpenGLApplication.Companion.context
import java.io.*


class Utils {
    companion object {

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
            return GLES30.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES30.glShaderSource(shader, shaderCode)
                GLES30.glCompileShader(shader)

                // check shader program
                val compileStatus = IntArray(1)
                GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0)

                if (compileStatus[0] == 0) {
                    Log.e("zhangbo", "loadShader $type Error")
                    Log.e("zhangbo", GLES30.glGetShaderInfoLog(shader))
                    GLES30.glDeleteShader(shader)
                }
            }
        }

        fun readStringFromRaw(@RawRes resId:Int): String {
            return runCatching{
                val builder=StringBuilder()
                val reader= BufferedReader(InputStreamReader(context.resources.openRawResource(resId)))//InputStreamReader它使用指定的字符集读取字节并将它们解码为字符//InputStream openRawResource(int id) 获取资源的数据流，读取资源数据
                var nextLine: String?=reader.readLine()
                while (nextLine !=null){
                    builder.append(nextLine).append("\n")
                    nextLine=reader.readLine()
                }
                reader.close()
                builder.toString()

            }.onFailure {
                when(it){
                    is IOException ->{
                        throw RuntimeException("Could not open resource: $resId",it)
                    }
                    is Resources.NotFoundException ->{
                        throw RuntimeException("Resource not found: $resId",it)
                    }
                    else ->{}
                }
            }.getOrThrow()//如果此实例表示成功，则返回封装的值;如果实例失败，则引发封装的 Throwable 异常

        }
    }
}