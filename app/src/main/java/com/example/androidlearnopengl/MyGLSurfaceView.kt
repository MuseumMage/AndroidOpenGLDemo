package com.example.androidlearnopengl

import android.content.Context
import android.opengl.GLSurfaceView
import com.example.androidlearnopengl.renderers.AndroidDocRenderer


class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: AndroidDocRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = AndroidDocRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}