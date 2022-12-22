package com.example.androidlearnopengl.fragments

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidlearnopengl.renderers.AndroidDocRenderer


class AndroidDocFragment : Fragment() {

    private lateinit var gLView: GLSurfaceView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gLView = GLSurfaceView(activity).apply {
            setEGLContextClientVersion(2)
            setRenderer(AndroidDocRenderer())
        }
        return gLView
    }
}