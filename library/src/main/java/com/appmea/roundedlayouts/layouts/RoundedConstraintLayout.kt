package com.appmea.roundedlayouts.layouts

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.appmea.roundedlayouts.ColorUtils
import com.appmea.roundedlayouts.LayoutVersionImplementation
import com.appmea.roundedlayouts.R

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
    private var rlBackgroundColor = DEFAULT_BACKGROUND_COLOR
    private val colorUtils: ColorUtils = ColorUtils(context)

    private var cornerRadius = DEFAULT_RADIUS

    private val layoutVersionImplementation: LayoutVersionImplementation


    init {
        layoutVersionImplementation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PostLollipop()
        } else {
            PreLollipop()
        }

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundedConstraintLayout)
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius)) {
                cornerRadius = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius,
                    DEFAULT_RADIUS
                )
            }

            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_stroke_width)) {
                strokeWidth = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_stroke_width,
                    DEFAULT_STROKE_WIDTH
                )
            }
            strokeWidthDouble = strokeWidth * 2f
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_stroke_color)) {
                strokeColor = array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_stroke_color,
                    DEFAULT_STROKE_COLOR
                )
            }

            rlBackgroundColor = if (isInEditMode) {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_background_color,
                    DEFAULT_BACKGROUND_COLOR
                )
            } else {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_background_color,
                    colorUtils.colorSurface
                )
            }
            array.recycle()
        }



        paintBackground = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.FILL
            color = rlBackgroundColor
            strokeWidth = strokeWidthDouble
        }

        paintStroke = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            color = strokeColor
            strokeWidth = strokeWidthDouble
        }



        layoutVersionImplementation.initBackground()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutVersionImplementation.onLayout(changed, left, top, right, bottom)
    }


    override fun dispatchDraw(canvas: Canvas) {
        layoutVersionImplementation.dispatchDraw(canvas)
    }

    fun callSuperDispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }


    /**
     * Layout implementation for Build.Version >= Lollipop
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class PostLollipop : LayoutVersionImplementation {
        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            pathStroke.reset()
            pathStroke.addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                Path.Direction.CW
            )
        }

        override fun dispatchDraw(canvas: Canvas) {
            canvas.save()
            this@RoundedConstraintLayout.callSuperDispatchDraw(canvas)
            canvas.restore()
            canvas.drawPath(pathStroke, paintStroke)
        }

        override fun initBackground() {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
            outlineProvider = ViewOutlineProvider.BACKGROUND
            val shapeDrawable = GradientDrawable()
            shapeDrawable.cornerRadius = cornerRadius.toFloat()
            shapeDrawable.setColor(rlBackgroundColor)
            background = shapeDrawable
            clipToOutline = true
        }
    }

    /**
     * Layout implementation for Build.Version < Lollipop
     */
    inner class PreLollipop : LayoutVersionImplementation {
        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            pathStroke.reset()
            pathStroke.addPath(
                createRoundedRect(
                    strokeWidth.toFloat(),
                    strokeWidth.toFloat(),
                    width.toFloat() - strokeWidth.toFloat(),
                    height.toFloat() - strokeWidth.toFloat(),
                    cornerRadius.toFloat(),
                    cornerRadius.toFloat()
                )
            )
        }

        override fun dispatchDraw(canvas: Canvas) {
            val count = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            paintBackground.xfermode = null
            canvas.drawPath(pathStroke, paintBackground)
            this@RoundedConstraintLayout.callSuperDispatchDraw(canvas)
            paintBackground.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawPath(pathStroke, paintBackground)
            paintStroke.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            canvas.drawPath(pathStroke, paintStroke)
            canvas.restoreToCount(count)
        }

        override fun initBackground() {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
        }

        private fun createRoundedRect(
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,
            rx: Float,
            ry: Float,
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
            path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
            path.rLineTo(widthMinusCorners, 0f)
            path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
            path.rLineTo(0f, -heightMinusCorners)
            path.close() //Given close, last lineto can be removed.
            return path
        }

    }
}