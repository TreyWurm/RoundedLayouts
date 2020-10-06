package com.appmea.roundedlayouts

import android.graphics.Path

interface RoundedLayout {
    companion object {
        const val DEFAULT_RADIUS = 48
        const val DEFAULT_STROKE_WIDTH = 0
        const val DEFAULT_STROKE_COLOR = 0x1F000000
        const val DEFAULT_BACKGROUND_COLOR = -0x1
    }


    fun createRoundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        rx: Float,
        ry: Float,
        symmetricCorners: Boolean = true
    ): Path {
        val path = Path()
        val width = right - left
        val height = bottom - top

        // Need to limit radii
        val widthHalf = width / 2f
        val heightHalf = height / 2f
        val rxLimited = rx.coerceAtLeast(0f).coerceAtMost(if (symmetricCorners) minOf(widthHalf, heightHalf) else width / 2f)
        val ryLimited = rx.coerceAtLeast(0f).coerceAtMost(if (symmetricCorners) minOf(widthHalf, heightHalf) else height / 2f)

        val widthMinusCorners = width - 2 * rxLimited
        val heightMinusCorners = height - 2 * ryLimited

        path.moveTo(right, top + ryLimited)
        path.rQuadTo(0f, -ryLimited, -rxLimited, -ryLimited) //top-right corner
        path.rLineTo(-widthMinusCorners, 0f)
        path.rQuadTo(-rxLimited, 0f, -rxLimited, ryLimited) //top-left corner
        path.rLineTo(0f, heightMinusCorners)
        path.rQuadTo(0f, ryLimited, rxLimited, ryLimited) //bottom-left corner
        path.rLineTo(widthMinusCorners, 0f)
        path.rQuadTo(rxLimited, 0f, rxLimited, -ryLimited) //bottom-right corner
        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }
}