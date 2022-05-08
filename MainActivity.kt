package com.example.cassebrique

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ImageView

class MainActivity: AppCompatActivity() {

    lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.vMain)
        var un = findViewById<View>(R.id.imageView1)
        var deux = findViewById<View>(R.id.imageView2)
        var trois = findViewById<View>(R.id.imageView3)
        drawingView.un = un as ImageView
        drawingView.deux = deux as ImageView
        drawingView.trois = trois as ImageView

    }
    override fun onPause() {
        super.onPause()
        drawingView.pause()
    }

    override fun onResume() {
        super.onResume()
        drawingView.resume()

    }



}
