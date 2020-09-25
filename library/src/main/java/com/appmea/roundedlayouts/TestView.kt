package com.appmea.roundedlayouts

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class TestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_RADIUS = 48
        const val DEFAULT_STROKE_WIDTH = 8
        const val DEFAULT_STROKE_COLOR = 0x1F000000
        const val DEFAULT_BACKGROUND_COLOR = -0x1
    }

    private val pathStroke = Path()
    private val paintStroke: Paint


    /**
     * With of border stroke
     */
    private var strokeWidth = DEFAULT_STROKE_WIDTH

    /**
     * Double the width of the border stroke, as drawing a path is using thickness/half as actual path middle
     */
    private var strokeWidthDouble = DEFAULT_STROKE_WIDTH * 2f
    private var strokeColor = DEFAULT_STROKE_COLOR
    private var rclBackgroundColor = DEFAULT_BACKGROUND_COLOR
    private val colorUtils: ColorUtils = ColorUtils(context)

    private var cornerRadius = DEFAULT_RADIUS


    init {
        paintStroke = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            color = strokeColor
            strokeWidth = strokeWidthDouble
        }


    }



    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        pathStroke.reset()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pathStroke.addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                Path.Direction.CW
            )
        } else {
            pathStroke.addPath(
                createRoundedRect(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    cornerRadius.toFloat(),
                    cornerRadius.toFloat(),
                    false
                )
            )
        }
    }


    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        super.dispatchDraw(canvas)
        canvas.restore()
        canvas.drawPath(pathStroke, paintStroke)
    }

    private fun createRoundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        rx: Float,
        ry: Float,
        conformToOriginalPost: Boolean
    ): Path {
        var rx = rx
        var ry = ry
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry
        path.moveTo(10f,10f)
        path.rQuadTo(10f,10f,10f,10f)
//        path.moveTo(right, top + ry)
//        path.rQuadTo(0f, -ry, -rx, -ry) //top-right corner
//        path.rLineTo(-widthMinusCorners, 0f)
//        path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
//        path.rLineTo(0f, heightMinusCorners)
//        if (conformToOriginalPost) {
//            path.rLineTo(0f, ry)
//            path.rLineTo(width, 0f)
//            path.rLineTo(0f, -ry)
//        } else {
//            path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
//            path.rLineTo(widthMinusCorners, 0f)
//            path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
//        }
//        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }
}