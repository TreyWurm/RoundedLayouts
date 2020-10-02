package com.appmea.roundedlayouts.example

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appmea.roundedlayouts.layouts.RoundedConstraintLayout
import com.appmea.roundedlayouts.layouts.RoundedLinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rclContainer = findViewById<RoundedConstraintLayout>(R.id.rcl_container)

        findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener { }
            text = Build.VERSION.SDK_INT.toString()
        }
    }
}