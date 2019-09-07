package de.ulrich_boeing.fitness_tools

import de.ulrich_boeing.basics.Color
import kotlin.math.roundToInt

class ColorRange(val start: Color, end: Color) {
    private val redRange = IntRange(start.red, end.red)
    private val greenRange = IntRange(start.green, end.green)
    private val blueRange = IntRange(start.blue, end.blue)

    fun random(): Color = Color.fromRGBA(redRange.random().toInt(), greenRange.random().toInt(), blueRange.random().toInt(), 210)

    fun mutate(color: Color, range: Float): Color {
        val red = redRange.mutate(color.red, range)
        val green = greenRange.mutate(color.green, range)
        val blue = blueRange.mutate(color.blue, range)
        val alpha= 210
        return Color.fromRGBA(red, green, blue, alpha)
    }
}