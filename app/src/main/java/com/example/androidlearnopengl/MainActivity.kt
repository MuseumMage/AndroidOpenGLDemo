package com.example.androidlearnopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.androidlearnopengl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.drawTriangleButton.setOnClickListener(View.OnClickListener {
//
//        })
    }

    // 1. create linear layout view with fragment
    // 2. create navigation button
    // 3. create opengl surface view
}