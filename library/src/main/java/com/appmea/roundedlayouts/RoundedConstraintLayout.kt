package com.appmea.roundedlayouts

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class RoundedConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_RADIUS = 48
        const val DEFAULT_STROKE_WIDTH = 0
        const val DEFAULT_STROKE_COLOR = 0x1F000000
        const val DEFAULT_BACKGROUND_COLOR = -0x1
    }

    private val pathStroke = Path()
    private val paintStroke: Paint

    private val pathBackground = Path()
    private val paintBackground: Paint

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
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundedConstraintLayout)
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_radius)) {
                cornerRadius = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rcl_radius,
                    DEFAULT_RADIUS
                )
            }

            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_stroke_width)) {
                strokeWidth = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rcl_stroke_width,
                    DEFAULT_STROKE_WIDTH
                )
            }
            strokeWidthDouble = strokeWidth * 2f
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_stroke_color)) {
                strokeColor = array.getColor(
                    R.styleable.RoundedConstraintLayout_rcl_stroke_color,
                    DEFAULT_STROKE_COLOR
                )
            }

            rclBackgroundColor = if (isInEditMode) {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rcl_background_color,
                    DEFAULT_BACKGROUND_COLOR
                )
            } else {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rcl_background_color,
                    colorUtils.colorSurface
                )
            }
            array.recycle()
        }

        paintBackground = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color = ContextCompat.getColor(context, android.R.color.white)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        paintStroke = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            color = strokeColor
            strokeWidth = strokeWidthDouble
        }

        setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initBackground21()
            clipToOutline = true
        } else {
            initBackground()
        }
    }

    private fun initBackground() {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.cornerRadius = cornerRadius.toFloat()
        shapeDrawable.setColor(rclBackgroundColor)
//        background = shapeDrawable
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initBackground21() {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        val shapeDrawable = GradientDrawable()
        shapeDrawable.cornerRadius = cornerRadius.toFloat()
        shapeDrawable.setColor(rclBackgroundColor)
        background = shapeDrawable
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
        path.moveTo(right, top + ry)
        path.rQuadTo(0f, -ry, -rx, -ry) //top-right corner
        path.rLineTo(-widthMinusCorners, 0f)
        path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
        path.rLineTo(0f, heightMinusCorners)
        if (conformToOriginalPost) {
            path.rLineTo(0f, ry)
            path.rLineTo(width, 0f)
            path.rLineTo(0f, -ry)
        } else {
            path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
            path.rLineTo(widthMinusCorners, 0f)
            path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
        }
        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }
}