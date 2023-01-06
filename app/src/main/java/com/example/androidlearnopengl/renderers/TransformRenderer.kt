package com.example.androidlearnopengl.renderers

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.utils.Utils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

val verticesTransform = floatArrayOf(     // in counterclockwise order:
    // positions          // colors           // texture coords
    0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, // top right
    0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // bottom left
    -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f  // top left
)

val indicesTransform = byteArrayOf(
    0, 1, 3, // first triangle
    1, 2, 3  // second triangle
)

const val COORDS_PER_VERTEX_TRANSFORM = 8 // 与vertices数据类相关

class TransformRenderer : GLSurfaceView.Renderer {

    private lateinit var mShader: MyShader
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indicesBuffer: ByteBuffer
    private var mTexture1: Int = 0
    private var mTexture2: Int = 0

    private val stride: Int = COORDS_PER_VERTEX_TRANSFORM * Float.SIZE_BYTES // 4 bytes per vertex

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mShader = MyShader(R.raw.transform_vertex, R.raw.transform_fragment)
        // create byte buffer
        initVertexBuffer()
        // init texture
        initTexture()
        // init env
        initEnv()
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        // bind Texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture1)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture2)

        // transform
        val transform = FloatArray(16)
        Matrix.setIdentityM(transform, 0)
        Matrix.translateM(transform, 0, transform, 0, 0.5f, -0.5f, 0.0f)
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.090f * time.toInt()
        Matrix.rotateM(transform, 0, transform, 0, angle, 0.0f, 0.0f, 1.0f)

        // set transform
        mShader.setMatrix("transform", transform)

        GLES30.glDrawElements(GL10.GL_TRIANGLES, indicesTransform.size, GL10.GL_UNSIGNED_BYTE, indicesBuffer);
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }


    private fun initVertexBuffer() {
        vertexBuffer =
                // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(verticesTransform.size * Float.SIZE_BYTES).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(verticesTransform)
                    // set the buffer to read the first coordinate
                    //position(0)
                }
            }

        indicesBuffer =
            ByteBuffer.allocateDirect(indicesTransform.size).run {
                order(ByteOrder.nativeOrder())
                put(indicesTransform)
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

        // set different texture
        mShader.setInt("texture1", 0)
        mShader.setInt("texture2", 1)
    }

    private fun initTexture() {
        mTexture1 = Utils.loadTexture(R.drawable.container)
        mTexture2 = Utils.loadTexture(R.drawable.awesomeface)
    }

}