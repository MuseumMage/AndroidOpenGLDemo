package com.example.androidlearnopengl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class DrawTriangleFragment : Fragment() {

    private lateinit var gLView: MyGLSurfaceView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gLView = MyGLSurfaceView(requireActivity())
        return gLView
    }
}