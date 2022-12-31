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
    //private var mProgram: Int = -1

    private val vertexCount: Int = vertices.size /COORDS_PER_VERTEX1
    private val stride: Int = COORDS_PER_VERTEX1 * Float.SIZE_BYTES // 4 bytes per vertex

    private lateinit var mShader : MyShader

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // create shader
        mShader = MyShader(R.raw.hello_triangle_vertex, R.raw.hello_triangle_fragment)
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
        //GLES30.glUseProgram(mProgram)
        mShader.use()

        val posHandle = GLES30.glGetAttribLocation(mShader.getProgram(), "aPos").also {
            vertexBuffer.position(0)
            GLES30.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                stride,
                vertexBuffer
            )
            GLES30.glEnableVertexAttribArray(it)
        }

        val colorHandle = GLES30.glGetAttribLocation(mShader.getProgram(), "aColor").also {
            vertexBuffer.position(3)
            GLES30.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                stride,
                vertexBuffer
            )
            GLES30.glEnableVertexAttribArray(it)
        }

    }

    private fun draw() {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

//        GLES30.glDisableVertexAttribArray(posHandle)
//        GLES30.glDisableVertexAttribArray(colorHandle)
    }
}