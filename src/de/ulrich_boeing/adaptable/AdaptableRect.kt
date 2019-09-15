package de.ulrich_boeing.adaptable

import de.ulrich_boeing.basics.Clipping
import processing.core.PApplet
import java.lang.Float.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

class AdaptableRect(app: PApplet, frame: Clipping, var sizeFactor: Float, val aspectRatio: Float) : Adaptable(app, frame) {
    val size: Float
        get() = frame.size * sizeFactor

    val minSideLength: Float
        get() = sqrt(size / aspectRatio)

    val maxSideLength: Float
        get() = sqrt(size * aspectRatio)

    val maxWidth: Float
        get() = min(maxSideLength, frame.width.toFloat())

    val maxHeight: Float
        get() = min(maxSideLength, frame.height.toFloat())

    fun getSizedClipping(normWidth: Float): Clipping {
        val minWidth = size / maxHeight
        val width = (1 - normWidth) * minWidth + normWidth * maxWidth
        val height = size / width
        return Clipping(0f, 0f, width, height)
    }

    fun getClipping(normWidth: Float, normX: Float, normY:Float): Clipping {
        val clipping = getSizedClipping(normWidth)
        clipping.x = (normX * (frame.width - clipping.width)).roundToInt()
        clipping.y = (normY * (frame.height - clipping.height)).roundToInt()
        return clipping
    }
}