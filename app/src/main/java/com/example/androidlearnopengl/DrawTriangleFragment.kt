package com.example.androidlearnopengl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.androidlearnopengl.databinding.FragmentDrawTriangleBinding


class DrawTriangleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDrawTriangleBinding>(
            inflater,
            R.layout.fragment_draw_triangle,
            container,
            false
        )
        return binding.root
    }
}