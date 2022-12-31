package com.example.androidlearnopengl.renderers

import android.content.res.Resources
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES30
import android.util.Log
import androidx.annotation.RawRes
import com.example.androidlearnopengl.OpenGLApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MyShader(private val vertexShaderID:Int, private val fragShaderID:Int) {
    // 程序ID
    private var programID:Int = 0

    init {
        val vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, readStringFromRaw(vertexShaderID))
        val fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, readStringFromRaw(fragShaderID))
        // create empty OpenGL ES Program
        programID = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)

        }

        // check shader program
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(programID, GLES30.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Log.e("zhangbo", "compileShader: Error")
            Log.e("zhangbo", GLES30.glGetProgramInfoLog(programID))
            GLES30.glDeleteProgram(programID)
        }
    }

    // 使用/激活程序
    fun use() {
        GLES30.glUseProgram(programID)
    }

    fun getProgram(): Int{
        return programID
    }

    fun setBool(name: String, value: Boolean) {
        GLES30.glUniform1i(glGetUniformLocation(programID, name), value.toInt())
    }
    private fun Boolean.toInt() = if (this) 1 else 0

    fun setInt(name: String, value:Int)
    {
        GLES30.glUniform1i(glGetUniformLocation(programID, name), value)
    }

    fun setFloat(name: String, value:Float)
    {
        GLES30.glUniform1f(glGetUniformLocation(programID, name), value)
    }


    private fun readStringFromRaw(@RawRes resId:Int): String {
        return runCatching{
            val builder=StringBuilder()
            val reader= BufferedReader(InputStreamReader(OpenGLApplication.context.resources.openRawResource(resId)))//InputStreamReader它使用指定的字符集读取字节并将它们解码为字符//InputStream openRawResource(int id) 获取资源的数据流，读取资源数据
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

    private fun compileShader(type: Int, shaderCode: String): Int {

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
}