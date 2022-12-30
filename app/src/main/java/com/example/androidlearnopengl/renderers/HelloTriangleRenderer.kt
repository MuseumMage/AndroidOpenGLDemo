package com.example.androidlearnopengl.renderers

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.utils.Utils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


val vertices = floatArrayOf(     // in counterclockwise order:
    // positions         // colors
    0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  // bottom left
    0.0f, 0.5f, 0.0f,   0.0f, 0.0f, 1.0f   // top
)

const val COORDS_PER_VERTEX1 = 6 // 与vertices数据类相关

class HelloTriangleRenderer : GLSurfaceView.Renderer {

    private lateinit var vertexBuffer: FloatBuffer
    private var mProgram: Int = -1

    private val vertexCount: Int = vertices.size /COORDS_PER_VERTEX1
    private val stride: Int = COORDS_PER_VERTEX1 * Float.SIZE_BYTES // 4 bytes per vertex

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // compile shader
        compileShader()
        // create byte buffer
        initVertexBuffer()
        // init env
        initEnv()
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private fun compileShader() {

        val vertexShaderCode = Utils.readStringFromRaw(R.raw.hello_triangle_vertex)
        val fragmentShaderCode = Utils.readStringFromRaw(R.raw.hello_triangle_fragment)

        val vertexShader: Int = Utils.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = Utils.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)

        }

        // check shader program
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Log.e("zhangbo", "compileShader: Error")
            Log.e("zhangbo", GLES30.glGetProgramInfoLog(mProgram))
            GLES30.glDeleteProgram(mProgram)
            return
        }
    }

    private fun initVertexBuffer() {
        vertexBuffer =
                // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(vertices.size * Float.SIZE_BYTES).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertices)
                    // set the buffer to read the first coordinate
                    //position(0)
                }
            }
    }

    private fun initEnv() {
        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)

        vertexBuffer.position(0)
        val posHandle = GLES30.glGetAttribLocation(mProgram, "aPos")
        GLES30.glVertexAttribPointer(
            posHandle,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(posHandle)

        vertexBuffer.position(3)
        val colorHandle = GLES30.glGetAttribLocation(mProgram, "aColor")
        GLES30.glVertexAttribPointer(
            colorHandle,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )
        GLES30.glEnableVertexAttribArray(colorHandle)
    }

    private fun draw() {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

//        GLES30.glDisableVertexAttribArray(posHandle)
//        GLES30.glDisableVertexAttribArray(colorHandle)
    }
}