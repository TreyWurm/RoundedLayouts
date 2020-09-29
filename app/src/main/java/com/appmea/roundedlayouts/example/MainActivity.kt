package com.appmea.roundedlayouts.example

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_1).apply {
            setOnClickListener { }
            text = Build.VERSION.SDK_INT.toString()
        }
        findViewById<TextView>(R.id.tv_2).setOnClickListener { }

//        val spinner = findViewById<Spinner>(R.id.sp_spinner)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, PorterDuff.Mode.values())
//        spinner.adapter = adapter
//        val testView = findViewById<TestView>(R.id.tv_test).apply { porterDuffMode = adapter.getItem(0) ?: PorterDuff.Mode.SRC }
//        spinner.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                testView.porterDuffMode = adapter.getItem(position)?: PorterDuff.Mode.SRC
//                testView.invalidate()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//        }
    }
}