package com.example.androidlearnopengl.fragments

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.androidlearnopengl.R
import com.example.androidlearnopengl.renderers.TextureRenderer
import com.example.androidlearnopengl.renderers.TransformRenderer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransformFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransformFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mRender: TransformRenderer

    private var mOldX: Float = 0.0f
    private var mOldY: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mRender = TransformRenderer()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = GLSurfaceView(activity).apply {
            setEGLContextClientVersion(3)
            setRenderer(mRender)
            setOnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_MOVE) {
                    //Log.d("zhangbo", "moveMove x: ${event.x}, y: ${event.y}")
                    mRender.setTouchStatus(MotionEvent.ACTION_MOVE)
                    mRender.setDeltaLoc(event.x - mOldX, event.y - mOldY)
                }
                if (event?.action == MotionEvent.ACTION_DOWN) {
//                    Log.d("zhangbo", "moveDown x: ${event.x}, y: ${event.y}")
                    mRender.setTouchStatus(MotionEvent.ACTION_DOWN)
                    mOldX = event.x
                    mOldY = event.y
                    mRender.setDeltaLoc(0.0f, 0.0f)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
//                    Log.d("zhangbo", "moveUp x: ${event.x}, y: ${event.y}")
                    mRender.setTouchStatus(MotionEvent.ACTION_UP)
                    mRender.setDeltaLoc(0.0f, 0.0f)
                }
                true
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransformFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransformFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}