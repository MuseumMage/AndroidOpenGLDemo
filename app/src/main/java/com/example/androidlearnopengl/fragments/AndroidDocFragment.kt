package com.example.androidlearnopengl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidlearnopengl.MyGLSurfaceView


class AndroidDocFragment : Fragment() {

    private lateinit var gLView: MyGLSurfaceView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gLView = MyGLSurfaceView(requireActivity())
        return gLView
    }
}