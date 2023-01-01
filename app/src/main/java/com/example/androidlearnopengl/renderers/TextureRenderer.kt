package com.example.androidlearnopengl.renderers

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.utils.Utils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

val verticesTexture = floatArrayOf(     // in counterclockwise order:
    // positions          // colors           // texture coords
    0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
    0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
    -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
    -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // top left
)

val indices = byteArrayOf(
    0, 1, 3, // first triangle
    1, 2, 3  // second triangle
)

const val COORDS_PER_VERTEX_TEXTURE = 8 // 与vertices数据类相关

class TextureRenderer : GLSurfaceView.Renderer  {

    private lateinit var mShader: MyShader
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indicesBuffer: ByteBuffer
    private var mTexture: Int = 0

    private val stride: Int = COORDS_PER_VERTEX_TEXTURE * Float.SIZE_BYTES // 4 bytes per vertex

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mShader = MyShader(R.raw.texture_vertex, R.raw.texture_fragment)
        // create byte buffer
        initVertexBuffer()
        // init texture
        initTexture()
        // init env
        initEnv()
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // bind Texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture)
        val textureShader = GLES30.glGetUniformLocation(mShader.getProgram(), "ourTexture").also {
            GLES30.glUniform1i(it, 0)
        }

        GLES30.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_BYTE, indicesBuffer);
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private fun initVertexBuffer() {
        vertexBuffer =
                // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(verticesTexture.size * Float.SIZE_BYTES).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(verticesTexture)
                    // set the buffer to read the first coordinate
                    //position(0)
                }
            }

        indicesBuffer =
            ByteBuffer.allocateDirect(indices.size).run {
                order(ByteOrder.nativeOrder())
                put(indices)
                position(0) as ByteBuffer
            }
    }

    private fun initEnv() {
        // Add program to OpenGL ES environment
        mShader.use()

        // todo: 可以抽象到shader.kt中
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

        val textureHandle = GLES30.glGetAttribLocation(mShader.getProgram(), "aTexCoord").also {
            vertexBuffer.position(6)
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

    private fun initTexture() {
        mTexture = Utils.loadTexture(R.drawable.container)
    }
}