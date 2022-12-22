package com.example.androidlearnopengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidlearnopengl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

//        gLView = MyGLSurfaceView(this)
    }

    // 1. create linear layout view with fragment
    // 2. create navigation button
    // 3. create opengl surface view

//    fun getGLView(): GLSurfaceView {
//        return gLView
//    }
}