package com.appmea.roundedlayouts

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RoundedConstraintLayout

    : ConstraintLayout {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) : super(context)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        const val DEFAULT_RADIUS = 48
        const val DEFAULT_STROKE_WIDTH = 0
        const val DEFAULT_STROKE_COLOR = 0x1F000000
        const val DEFAULT_BACKGROUND_COLOR = -0x1
    }

    private var pathStroke = Path()
    private var paintStroke = Paint()

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
//        if (attrs != null) {
//            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundedConstraintLayout)
//            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_radius)) {
//                cornerRadius = array.getDimensionPixelSize(
//                    R.styleable.RoundedConstraintLayout_rcl_radius,
//                    DEFAULT_RADIUS
//                )
//            }
//
//            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_stroke_width)) {
//                strokeWidth = array.getDimensionPixelSize(
//                    R.styleable.RoundedConstraintLayout_rcl_stroke_width,
//                    DEFAULT_STROKE_WIDTH
//                )
//            }
//            strokeWidthDouble = strokeWidth * 2f
//            if (array.hasValue(R.styleable.RoundedConstraintLayout_rcl_stroke_color)) {
//                strokeColor = array.getColor(
//                    R.styleable.RoundedConstraintLayout_rcl_stroke_color,
//                    DEFAULT_STROKE_COLOR
//                )
//            }
//
//            rclBackgroundColor = if (isInEditMode) {
//                array.getColor(
//                    R.styleable.RoundedConstraintLayout_rcl_background_color,
//                    DEFAULT_BACKGROUND_COLOR
//                )
//            } else {
//                array.getColor(
//                    R.styleable.RoundedConstraintLayout_rcl_background_color,
//                    colorUtils.colorSurface
//                )
//            }
//            array.recycle()
//        }

        paintStroke.flags = Paint.ANTI_ALIAS_FLAG
        paintStroke.style = Paint.Style.STROKE
        paintStroke.color = strokeColor
        paintStroke.strokeWidth = strokeWidthDouble
        initBackground()
        setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
        clipToOutline = true
    }

    private fun initBackground() {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        val shapeDrawable = GradientDrawable()
        shapeDrawable.cornerRadius = cornerRadius.toFloat()
        shapeDrawable.setColor(rclBackgroundColor)
        background = shapeDrawable
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
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
        super.dispatchDraw(canvas)
        canvas.restore()
        canvas.drawPath(pathStroke, paintStroke)
    }
}