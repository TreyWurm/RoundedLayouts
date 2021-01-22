package com.appmea.roundedlayouts.example

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appmea.roundedlayouts.layouts.RoundedConstraintLayout
import eu.davidea.flexibleadapter.FlexibleAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var rclContainer = findViewById<RoundedConstraintLayout>(R.id.rcl_container)
//        rclContainer.cornerRadius = 0
//
//        findViewById<TextView>(R.id.tv_1).apply {
//            setOnClickListener { }
//            text = Build.VERSION.SDK_INT.toString()
//        }

//        var seekbar = findViewById<SeekBar>(R.id.sb_radius)
//        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                println("PROGRESS $progress")
//                rclContainer.cornerRadius = progress
//                val strokeColor = rclContainer.rlBackgroundColor
//                val alpha = (0xFF and (strokeColor shr 24)) / 255f
//                val red = (0xFF and (strokeColor shr 16)) / 255f
//                val green = (0xFF and (strokeColor shr 8)) / 255f
//                val blue = (0xFF and strokeColor) / 255f
//
//                rclContainer.rlBackgroundColor = Color.argb((alpha * 255f).toInt(), progress, (green * 255f).toInt(), (blue * 255f).toInt())
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//            }
//        })


        var recyclerView = findViewById<RecyclerView>(R.id.rv_items)
        val list = List(500) {
            SimpleItem(it.toString())
        }
        var adapter: FlexibleAdapter<SimpleItem> = FlexibleAdapter(list)
        recyclerView.addItemDecoration(MarginItemDecoration(48))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                left = spaceHeight
                right = spaceHeight
                bottom = spaceHeight
            }
        }
    }
}