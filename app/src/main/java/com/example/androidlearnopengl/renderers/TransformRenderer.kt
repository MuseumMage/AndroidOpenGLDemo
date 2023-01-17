package com.example.androidlearnopengl.renderers

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.utils.Utils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

val verticesTransform = floatArrayOf(     // in counterclockwise order:
    // positions          // texture coords
    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
    0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
    0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
    0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
    0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
    -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

    -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
    -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
    -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
    0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
    0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
    0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
    0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
    -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
    0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
)

//val indicesTransform = byteArrayOf(
//    0, 1, 3, // first triangle
//    1, 2, 3  // second triangle
//)

val cubePositions = arrayListOf(
    floatArrayOf(0.0f, 0.0f, 0.0f),
    floatArrayOf(2.0f, 5.0f, -15.0f),
    floatArrayOf(-1.5f, -2.2f, -2.5f),
    floatArrayOf(-3.8f, -2.0f, -12.3f),
    floatArrayOf(2.4f, -0.4f, -3.5f),
    floatArrayOf(-1.7f, 3.0f, -7.5f),
    floatArrayOf(1.3f, -2.0f, -2.5f),
    floatArrayOf(1.5f, 2.0f, -2.5f),
    floatArrayOf(1.5f, 0.2f, -1.5f),
    floatArrayOf(-1.3f, 1.0f, -1.5f)
)

const val COORDS_PER_VERTEX_TRANSFORM = 5 // 与vertices数据类相关

class TransformRenderer : GLSurfaceView.Renderer {

    private lateinit var mShader: MyShader
    private lateinit var vertexBuffer: FloatBuffer

    //private lateinit var indicesBuffer: ByteBuffer
    private val vertexCount: Int = verticesTransform.size / COORDS_PER_VERTEX_TRANSFORM
    private var mRatio: Float = 0.0f
    private var mWidth: Float = 0.0f
    private var mHeight: Float = 0.0f
    private var mTexture1: Int = 0
    private var mTexture2: Int = 0
    private var mX: Float = 0.0f
    private var mY: Float = 0.0f
    private var mDeltaX: Float = 0.0f
    private var mDeltaY: Float = 0.0f
    private var mCamPos: FloatArray = floatArrayOf(0.0f, 0.0f, 3.0f)
    private var mCamPosPre: FloatArray = floatArrayOf(0.0f, 0.0f, 3.0f)

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
        // Depth Testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT)
        // bind Texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture1)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture2)

        // transform chapter
//        val transform = FloatArray(16)
//        Matrix.setIdentityM(transform, 0)
//        Matrix.translateM(transform, 0, transform, 0, 0.5f, -0.5f, 0.0f)
//        val time = SystemClock.uptimeMillis() % 4000L
//        val angle = 0.090f * time.toInt()
//        Matrix.rotateM(transform, 0, transform, 0, angle, 0.0f, 0.0f, 1.0f)

        // MVP Matrix
//        val modelMatrix = FloatArray(16).also {
//            val time = SystemClock.uptimeMillis() % 4000L
//            val angle = 0.090f * time.toInt()
//            Matrix.setRotateM(it, 0, angle, 0.5f, 1.0f, 0.0f)
//        }
        val modelMatrix = FloatArray(16)
        // set cam view
        val viewMatrix = FloatArray(16).also {
//            Matrix.setIdentityM(it,0)
//            Matrix.translateM(it, 0, 0.0f, 0.0f, -3.0f)
//            val time = SystemClock.uptimeMillis() % 4000L * 0.00090f
//            val camX = sin(time) * 10f
//            val camZ = cos(time) * 10f
            val transX = mDeltaX / mWidth / 2
            val transY = -mDeltaY / mHeight / 2

            val cameraPos = floatArrayOf(mCamPos[0] - transX, mCamPos[1] - transY, 0.0f)
            val cameraFront = floatArrayOf(0.0f, 0.0f, -1.0f)
            val cameraUp = floatArrayOf(0.0f, 1.0f, 0.0f)
            val center = floatArrayOf(cameraPos[0] + cameraFront[0], cameraPos[1] + cameraFront[1], cameraPos[2] + cameraFront[2])
            mCamPosPre = cameraPos

            Matrix.setLookAtM(it, 0, cameraPos[0], cameraPos[1], cameraPos[2], center[0], center[1], center[2], cameraUp[0], cameraUp[1], cameraUp[2])
//            Log.d("zhangbo", "mCamPosPre x: ${mCamPosPre[0]}, y: ${mCamPosPre[0]}")
//            Log.d("zhangbo", "mCamPos x: ${mCamPos[0]}, y: ${mCamPos[0]}")
        }
        val projectionMatrix = FloatArray(16).also {
            Matrix.perspectiveM(it, 0, 45.0f, mRatio, 0.1f, 100.0f)
        }

        // set transform
//        mShader.setMatrix("transform", transform)
//        mShader.setMatrix("model", modelMatrix)
        mShader.setMatrix("view", viewMatrix)
        mShader.setMatrix("projection", projectionMatrix)

        // draw 10 cubes
        for ((index, cube) in cubePositions.withIndex()) {
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.translateM(modelMatrix, 0, cube[0], cube[1], cube[2])

            val angle = 20.0f * index
            Matrix.rotateM(modelMatrix, 0, modelMatrix, 0, angle, 1.0f, 0.3f, 0.5f)
            mShader.setMatrix("model", modelMatrix)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)
        }
//        GLES30.glDrawElements(GL10.GL_TRIANGLES, indicesTransform.size, GL10.GL_UNSIGNED_BYTE, indicesBuffer);
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mRatio = width.toFloat() / height.toFloat()
        mWidth = width.toFloat()
        mHeight = height.toFloat()
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

//        indicesBuffer =
//            ByteBuffer.allocateDirect(indicesTransform.size).run {
//                order(ByteOrder.nativeOrder())
//                put(indicesTransform)
//                position(0) as ByteBuffer
//            }
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

        val textureHandle = GLES30.glGetAttribLocation(mShader.getProgram(), "aTexCoord").also {
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

        // set different texture
        mShader.setInt("texture1", 0)
        mShader.setInt("texture2", 1)
    }

    private fun initTexture() {
        mTexture1 = Utils.loadTexture(R.drawable.container)
        mTexture2 = Utils.loadTexture(R.drawable.awesomeface)
    }

    fun setTouchLoc(x: Float, y: Float) {
        mX = x
        mY = y
    }

    fun setDeltaLoc(deltaX: Float, deltaY: Float) {
        mDeltaX = deltaX
        mDeltaY = deltaY
        Log.d("zhangbo", "mCamPosPre x: ${mCamPosPre[0]}, y: ${mCamPosPre[0]}")
        Log.d("zhangbo", "mCamPos x: ${mCamPos[0]}, y: ${mCamPos[0]}")
    }

    fun setCamPos(x: Float, y: Float, z: Float) {
        mCamPos = pixelToGL(x, y, z)
        Log.d("zhangbo", "setCamPos x: ${mCamPos[0]}, y: ${mCamPos[1]}")
    }

    fun updateCamPos() {
        mCamPos = mCamPosPre
        Log.d("zhangbo", "mCamPosPre x: ${mCamPosPre[0]}, y: ${mCamPosPre[0]}")
        Log.d("zhangbo", "mCamPos x: ${mCamPos[0]}, y: ${mCamPos[0]}")
    }

    private fun pixelToGL(x: Float, y: Float, z: Float): FloatArray {
        return floatArrayOf(x / mWidth / 2, y / mHeight / 2, z)
    }
}