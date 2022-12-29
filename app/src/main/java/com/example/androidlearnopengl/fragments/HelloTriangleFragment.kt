package com.example.androidlearnopengl.fragments

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.renderers.AndroidDocRenderer
import com.example.androidlearnopengl.renderers.HelloTriangleRenderer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloTriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HelloTriangleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return GLSurfaceView(activity).apply {
            setEGLContextClientVersion(2)
            setRenderer(HelloTriangleRenderer())
        }
    }

}