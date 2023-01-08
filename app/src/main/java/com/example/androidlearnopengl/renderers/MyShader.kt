package com.example.androidlearnopengl.renderers

import android.content.res.Resources
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES30
import android.util.Log
import androidx.annotation.RawRes
import com.example.androidlearnopengl.OpenGLApplication
import com.example.androidlearnopengl.utils.Utils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MyShader(private val vertexShaderID:Int, private val fragShaderID:Int) {

    private val TAG = "MyShader"

    // 程序ID
    private var programID:Int = 0

    init {
        val vertexShader = compileShader(GLES30.GL_VERTEX_SHADER, Utils.readStringFromRaw(vertexShaderID))
        val fragmentShader = compileShader(GLES30.GL_FRAGMENT_SHADER, Utils.readStringFromRaw(fragShaderID))
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
        val handle = glGetUniformLocation(programID, name).also {
            if (it == -1) {
                Log.e(TAG, "setBool: cannot find $name")
                return
            }
            GLES30.glUniform1i(it, value.toInt())
        }
    }
    private fun Boolean.toInt() = if (this) 1 else 0

    fun setInt(name: String, value:Int)
    {
        val handle = glGetUniformLocation(programID, name).also {
            if (it == -1) {
                Log.e(TAG, "setInt: cannot find $name")
                return
            }
            GLES30.glUniform1i(it, value)
        }
    }

    fun setFloat(name: String, value:Float)
    {
        val handle = glGetUniformLocation(programID, name).also {
            if (it == -1) {
                Log.e(TAG, "setFloat: cannot find $name")
                return
            }
            GLES30.glUniform1f(it, value)
        }
    }

    fun setMatrix(name: String, value:FloatArray)
    {
        val handle = glGetUniformLocation(programID, name).also {
            if (it == -1) {
                Log.e(TAG, "setMatrix: cannot find $name")
                return
            }
            GLES30.glUniformMatrix4fv(it, 1, false, value, 0)
        }
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