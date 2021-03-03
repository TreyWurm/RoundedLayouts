package com.appmea.roundedlayouts.layouts

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.appmea.colorutils.MaterialColorUtils
import com.appmea.roundedlayouts.LayoutVersionImplementation
import com.appmea.roundedlayouts.R
import com.appmea.roundedlayouts.RoundedLayout
import com.appmea.roundedlayouts.RoundedLayout.Companion.DEFAULT_BACKGROUND_COLOR
import com.appmea.roundedlayouts.RoundedLayout.Companion.DEFAULT_RADIUS
import com.appmea.roundedlayouts.RoundedLayout.Companion.DEFAULT_STROKE_COLOR
import com.appmea.roundedlayouts.RoundedLayout.Companion.DEFAULT_STROKE_WIDTH

open class RoundedLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), RoundedLayout {

    /**
     * Double the width of the border stroke, as drawing a path is using thickness/half as actual path middle
     */
    private var strokeWidthDouble = DEFAULT_STROKE_WIDTH * 2f
    private val layoutVersionImplementation: LayoutVersionImplementation
    private val pathStroke = Path()
    private val paintStroke = Paint()
    private val paintBackground = Paint()
    private val colorUtils: MaterialColorUtils = MaterialColorUtils(context)


    final override var strokeWidth = DEFAULT_STROKE_WIDTH
        set(value) {
            strokeWidthDouble = value * 2f
            field = value
        }

    final override var strokeColor = DEFAULT_STROKE_COLOR
        set(value) {
            field = value
            paintStroke.color = value
            layoutVersionImplementation.initBackground()
            requestLayout()
            invalidate()
        }

    final override var cornerRadius = DEFAULT_RADIUS
        set(value) {
            field = value
            layoutVersionImplementation.initBackground()
            requestLayout()
        }

    final override var rlBackgroundColor = DEFAULT_BACKGROUND_COLOR
        set(value) {
            field = value
            paintBackground.color = value
            invalidate()
        }

    final override var rippleColor = colorUtils.darken(rlBackgroundColor, 0.2f)
        set(value) {
            field = value
            layoutVersionImplementation.initBackground()
            invalidate()
        }


    init {
        layoutVersionImplementation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PostLollipop()
        } else {
            PreLollipop()
        }

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout)
            if (array.hasValue(R.styleable.RoundedLinearLayout_rl_radius)) {
                cornerRadius = array.getDimensionPixelSize(
                    R.styleable.RoundedLinearLayout_rl_radius,
                    DEFAULT_RADIUS
                )
            }

            if (array.hasValue(R.styleable.RoundedLinearLayout_rl_stroke_width)) {
                strokeWidth = array.getDimensionPixelSize(
                    R.styleable.RoundedLinearLayout_rl_stroke_width,
                    DEFAULT_STROKE_WIDTH
                )
            }
            strokeWidthDouble = strokeWidth * 2f
            if (array.hasValue(R.styleable.RoundedLinearLayout_rl_stroke_color)) {
                strokeColor = array.getColor(
                    R.styleable.RoundedLinearLayout_rl_stroke_color,
                    DEFAULT_STROKE_COLOR
                )
            }

            rlBackgroundColor = if (isInEditMode) {
                array.getColor(
                    R.styleable.RoundedLinearLayout_rl_background_color,
                    DEFAULT_BACKGROUND_COLOR
                )
            } else {
                array.getColor(
                    R.styleable.RoundedLinearLayout_rl_background_color,
                    colorUtils.getColorSurface()
                )
            }
            array.recycle()
        }


        paintBackground.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.FILL
            color = rlBackgroundColor
            strokeWidth = strokeWidthDouble
        }

        paintStroke.apply {
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
            pathStroke.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
//            pathStroke.addRoundRect(
//                0f,
//                0f,
//                width.toFloat(),
//                height.toFloat(),
//                cornerRadius.toFloat(),
//                cornerRadius.toFloat(),
//                Path.Direction.CW
//            )
        }

        override fun dispatchDraw(canvas: Canvas) {
            canvas.save()
            this@RoundedLinearLayout.callSuperDispatchDraw(canvas)
            canvas.restore()
            canvas.drawPath(pathStroke, paintStroke)
        }

        override fun initBackground() {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
            outlineProvider = ViewOutlineProvider.BACKGROUND
            val shapeDrawable = GradientDrawable()
            shapeDrawable.cornerRadius = cornerRadius.toFloat()
            shapeDrawable.setColor(rlBackgroundColor)
            val rippleDrawable = RippleDrawable(ColorStateList.valueOf(rippleColor), shapeDrawable, null)
            background = rippleDrawable
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
            @Suppress("deprecation")
            val count = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            paintBackground.xfermode = null
            canvas.drawPath(pathStroke, paintBackground)
            this@RoundedLinearLayout.callSuperDispatchDraw(canvas)
            paintBackground.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawPath(pathStroke, paintBackground)
            paintStroke.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            canvas.drawPath(pathStroke, paintStroke)
            canvas.restoreToCount(count)
        }

        override fun initBackground() {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
        }
    }
}