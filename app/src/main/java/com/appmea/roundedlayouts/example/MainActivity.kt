package com.appmea.roundedlayouts.example

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appmea.roundedlayouts.example.databinding.ActivityMainBinding
import eu.davidea.flexibleadapter.FlexibleAdapter

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val singleItem = true
        binding.rclContainer.visibility = if (singleItem) View.VISIBLE else View.GONE
        binding.sbSeekbar.visibility = if (singleItem) View.VISIBLE else View.GONE
        binding.rvItems.visibility = if (singleItem) View.GONE else View.VISIBLE


        // ================================================================================================================================================================
        // Single item
        // ================================================================================================================================================================
        binding.rclContainer.cornerRadius = 0
        binding.rclContainer.rlBackgroundColor = Color.WHITE
        binding.rclContainer.setOnClickListener { }

        binding.tv1.apply {
            setOnClickListener { }
            text = Build.VERSION.SDK_INT.toString()
        }

        binding.sbSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                binding.rclContainer.cornerRadius = progress
                Log.e(TAG, "onProgressChanged: ${10 * progress / binding.sbSeekbar.max}")
                binding.rclContainer.strokeWidth = 50 * progress / binding.sbSeekbar.max
                val backgroundColor = binding.rclContainer.rlBackgroundColor
                val alpha = (0xFF and (backgroundColor shr 24)) / 255f
                val red = (0xFF and (backgroundColor shr 16)) / 255f
                val green = (0xFF and (backgroundColor shr 8)) / 255f
                val blue = (0xFF and backgroundColor) / 255f

//                binding.rclContainer.rlBackgroundColor = Color.argb((alpha * 255f).toInt(), progress, (green * 255f).toInt(), (blue * 255f).toInt())
//                binding.rclContainer.rippleColor = Color.rgb((red * 255f).toInt(), progress, (blue * 255f).toInt())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        // ================================================================================================================================================================
        // RV
        // ================================================================================================================================================================
        val list = List(500) {
            SimpleItem(it.toString())
        }
        var adapter: FlexibleAdapter<SimpleItem> = FlexibleAdapter(list)
        binding.rvItems.addItemDecoration(MarginItemDecoration(48))
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = adapter
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