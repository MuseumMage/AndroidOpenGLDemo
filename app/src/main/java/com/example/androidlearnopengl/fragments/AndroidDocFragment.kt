package com.example.androidlearnopengl.fragments

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidlearnopengl.renderers.AndroidDocRenderer


class AndroidDocFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return GLSurfaceView(activity).apply {
            setEGLContextClientVersion(3)
            setRenderer(AndroidDocRenderer())
        }
    }
}