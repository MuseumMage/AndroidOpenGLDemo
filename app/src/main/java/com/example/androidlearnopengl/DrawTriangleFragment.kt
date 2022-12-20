package com.example.androidlearnopengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.androidlearnopengl.databinding.FragmentDrawTriangleBinding


class DrawTriangleFragment : Fragment() {

    private lateinit var gLView: MyGLSurfaceView
    private lateinit var gLView2: GLSurfaceView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gLView2 = GLSurfaceView(activity)

        // Create an OpenGL ES 2.0 context
        gLView2.setEGLContextClientVersion(2)

        // Set the Renderer for drawing on the GLSurfaceView
        gLView2.setRenderer(MyRenderer())

        return gLView2
    }
}