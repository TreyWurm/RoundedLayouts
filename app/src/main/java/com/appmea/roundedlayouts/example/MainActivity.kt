package com.appmea.roundedlayouts.example

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appmea.roundedlayouts.TestView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener { }
            text = "New text 1"
        }
        findViewById<TextView>(R.id.tv_2).setOnClickListener { }
        findViewById<TestView>(R.id.tv_3).setOnClickListener { }
    }
}