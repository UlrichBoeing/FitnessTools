package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.COLOR_BLACK
import de.ulrich_boeing.basics.COLOR_BLUE
import de.ulrich_boeing.basics.Vec
import de.ulrich_boeing.canvas.CanvasLayer
import processing.core.PGraphics
import java.lang.RuntimeException

data class DrawableData(
    val color: Int = COLOR_BLUE,
    val targetColor: Int = COLOR_BLACK,
    val size: Float = 20f,
    val complexity: Float = 50f,
    val variance: Float = 50f,
    val minOpacity: Int = 10,
    val maxOpacity: Int = 60,
    val className: String = ""
) {
    fun sliderNames(): List<String> = listOf("size", "complexity", "variance", "minOpacity", "maxOpacity")
    fun getSliderMaxValues(): List<Float> = listOf(100f, 100f, 100f, 255f, 255f)
    fun getSliderValues(): List<Float> = listOf(size, complexity, variance, minOpacity.toFloat(), maxOpacity.toFloat())
}

abstract class Drawable(val position: Vec, val data: DrawableData) {
    companion object {
        lateinit var canvasLayer : CanvasLayer
        fun create(position: Vec, data: DrawableData): Drawable {
            return when (data.className) {
                "Line" -> Line(position, data)
                "SimpleCircle" -> SimpleCircle(position, data)
                "ComplexPolygon" -> ComplexPolygon(position, data)
                "FilterGrid" -> FilterGrid(position, data)
                else -> throw RuntimeException("'" + data.className + "' is a non-existing class")
            }
        }
    }

    abstract fun draw(g: PGraphics, size: Float)
}

fun MutableList<DrawableData>.complement(count: Int): MutableList<DrawableData> {
    return MutableList<DrawableData>(count) { i ->
        if (i < this.size)
            this[i]
        else
            this.last().copy()
    }
}