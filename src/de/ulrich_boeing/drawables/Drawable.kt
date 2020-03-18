package de.ulrich_boeing.drawables

import de.ulrich_boeing.basics.COLOR_BLACK
import de.ulrich_boeing.basics.Vec
import processing.core.PGraphics
import java.lang.RuntimeException

data class DrawableData(
    var color: Int = COLOR_BLACK,
    var targetColor: Int = COLOR_BLACK,
    var size: Float = 20f,
    var complexity: Float = 50f,
    var variance: Float = 50f,
    var minOpacity: Int = 10,
    var maxOpacity: Int = 60,
    var className: String = ""
) {
    fun sliderNames(): List<String> = listOf("size", "complexity", "variance", "minOpacity", "maxOpacity")
    fun getSliderMaxValues(): List<Float> = listOf(100f, 100f, 100f, 255f, 255f)
    fun getSliderValues(): List<Float> = listOf(size, complexity, variance, minOpacity.toFloat(), maxOpacity.toFloat())
}

abstract class Drawable(val position: Vec, val data: DrawableData) {
    companion object {
        fun create(position: Vec, data: DrawableData): Drawable {
            return when (data.className) {
                "SimpleCircle" -> SimpleCircle(position, data)
                "ComplexPolygon" -> ComplexPolygon(position, data)
                else -> throw RuntimeException("'" + data.className + "' is a non-existing class")
            }
        }
    }
    abstract fun draw(g: PGraphics, size: Float)
}