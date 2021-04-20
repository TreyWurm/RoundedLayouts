package com.appmea.roundedlayouts

import android.graphics.Path

interface RoundedLayout {
    companion object {
        const val DEFAULT_RADIUS = 0
        const val DEFAULT_STROKE_WIDTH = 0
        const val DEFAULT_STROKE_COLOR = 0x1F000000
        const val DEFAULT_BACKGROUND_COLOR = -0x1
    }

    var strokeColor: Int
    var strokeWidth: Int
    var cornerRadius: Int
    var cornerRadii: IntArray
    var rlBackgroundColor: Int
    var rippleColor: Int

    fun createRoundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        radii: FloatArray,
    ): Path {
        val path = Path()
        val width = right - left
        val height = bottom - top

        // Need to limit radii
        val widthHalf = width / 2f
        val heightHalf = height / 2f
        val rxLimitedTopLeft = radii[0].coerceAtLeast(0f).coerceAtMost(widthHalf)
        val ryLimitedTopLeft = radii[1].coerceAtLeast(0f).coerceAtMost(heightHalf)

        val rxLimitedTopRight = radii[2].coerceAtLeast(0f).coerceAtMost(widthHalf)
        val ryLimitedTopRight = radii[3].coerceAtLeast(0f).coerceAtMost(heightHalf)

        val rxLimitedBottomRight = radii[4].coerceAtLeast(0f).coerceAtMost(widthHalf)
        val ryLimitedBottomRight = radii[5].coerceAtLeast(0f).coerceAtMost(heightHalf)

        val rxLimitedBottomLeft = radii[6].coerceAtLeast(0f).coerceAtMost(widthHalf)
        val ryLimitedBottomLeft = radii[7].coerceAtLeast(0f).coerceAtMost(heightHalf)

        val pathWidthTop = width - rxLimitedTopLeft - rxLimitedTopRight
        val pathHeightLeft = height - ryLimitedTopLeft - ryLimitedBottomLeft
        val pathWidthBottom = width - rxLimitedBottomLeft - rxLimitedBottomRight
        val pathHeightRight = height - ryLimitedBottomRight - ryLimitedTopRight

        path.moveTo(right, top + ryLimitedTopRight)
        path.rQuadTo(0f, -ryLimitedTopRight, -rxLimitedTopRight, -ryLimitedTopRight) //top-right corner
        path.rLineTo(-pathWidthTop, 0f)

        path.rQuadTo(-rxLimitedTopLeft, 0f, -rxLimitedTopLeft, ryLimitedTopLeft) //top-left corner
        path.rLineTo(0f, pathHeightLeft)

        path.rQuadTo(0f, ryLimitedBottomLeft, rxLimitedBottomLeft, ryLimitedBottomLeft) //bottom-left corner
        path.rLineTo(pathWidthBottom, 0f)

        path.rQuadTo(rxLimitedBottomRight, 0f, rxLimitedBottomRight, -ryLimitedBottomRight) //bottom-right corner
        path.rLineTo(0f, -pathHeightRight)
        path.close() //Given close, last lineto can be removed.
        return path
    }

    fun IntArray.toFloatArray(): FloatArray {
        return this.map { it.toFloat() }.toFloatArray()
    }
}