package de.ulrich_boeing.fitness_tools

import de.ulrich_boeing.basics.Color
import kotlin.math.roundToInt

class ColorRange(val start: Color, end: Color) {
    val redRange = FloatRange(start.red.toFloat(), end.red.toFloat())
    val greenRange = FloatRange(start.green.toFloat(), end.green.toFloat())
    val blueRange = FloatRange(start.blue.toFloat(), end.blue.toFloat())

    fun random(): Color = Color.fromRGBA(redRange.random().toInt(), greenRange.random().toInt(), blueRange.random().toInt(), 200)

    fun mutate(color: Color, range: Float): Color {
        val red = redRange.mutate(color.red.toFloat(), range)
        val green = greenRange.mutate(color.green.toFloat(), range)
        val blue = blueRange.mutate(color.blue.toFloat(), range)
        val alpha= 200
        return Color.fromRGBA(red.roundToInt(), green.roundToInt(), blue.roundToInt(), alpha)
    }
}