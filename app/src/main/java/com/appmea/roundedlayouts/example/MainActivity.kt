package com.appmea.roundedlayouts.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appmea.roundedlayouts.RoundedConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var test =RoundedConstraintLayout(this)
    }
}