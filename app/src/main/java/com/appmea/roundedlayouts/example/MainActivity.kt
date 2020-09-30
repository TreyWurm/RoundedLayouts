package com.appmea.roundedlayouts.example

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener { }
            text = Build.VERSION.SDK_INT.toString()
        }
    }
}