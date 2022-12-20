package com.example.androidlearnopengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRenderer : GLSurfaceView.Renderer {

    private lateinit var mTriangle: Shape.Triangle
    private lateinit var mSquare: Shape.Square


    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
//        TODO("Not yet implemented")
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // initialize a triangle
        mTriangle = Shape.Triangle()
        // initialize a square
        mSquare = Shape.Square()
    }

    override fun onDrawFrame(p0: GL10?) {
//        TODO("Not yet implemented")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mTriangle.draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
//        TODO("Not yet implemented")
        GLES20.glViewport(0, 0, width, height)
    }

}