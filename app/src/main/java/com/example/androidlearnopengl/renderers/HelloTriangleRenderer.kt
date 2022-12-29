package com.example.androidlearnopengl.renderers

import android.opengl.GLES20
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
    0.0f, 0.5f, 0.0f,      // top
    -0.5f, -0.5f, 0.0f,    // bottom left
    0.5f, -0.5f, 0.0f      // bottom right
)


class HelloTriangleRenderer : GLSurfaceView.Renderer {

    private lateinit var vertexBuffer: FloatBuffer
    private var mProgram: Int = -1

    private val vertexCount: Int = vertices.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // compile shader
        compileShader()
        // create byte buffer
        initVertexBuffer()
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private fun compileShader() {

        val vertexShaderCode = Utils.readFile(R.raw.hello_triangle_vertex)
        val fragmentShaderCode = Utils.readFile(R.raw.hello_triangle_fragment)

        val vertexShader: Int = Utils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)

        }

        // check shader program
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(mProgram)
            Log.e("zhangbo", "compileShader: Error")
            return
        }
    }

    private fun initVertexBuffer() {
        vertexBuffer =
                // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(vertices.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertices)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }
    }

    private fun draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(mProgram, "aPos").let {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}