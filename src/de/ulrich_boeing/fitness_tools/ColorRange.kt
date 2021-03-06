package de.ulrich_boeing.fitness_tools

import de.ulrich_boeing.basics.Color

class ColorRange(val start: Color, end: Color) {
    private val redRange = IntegerRange(start.red, end.red)
    private val greenRange = IntegerRange(start.green, end.green)
    private val blueRange = IntegerRange(start.blue, end.blue)
    private val alpha = start.alpha

    fun random(): Color = Color.fromRGBA(redRange.random(), greenRange.random(), blueRange.random(), 210)

    fun mutate(color: Color, range: Float): Color {
        val red = redRange.mutate(color.red, range)
        val green = greenRange.mutate(color.green, range)
        val blue = blueRange.mutate(color.blue, range)
        return Color.fromRGBA(red, green, blue, alpha)
    }

    fun lerp(pos1: Float, pos2: Float, pos3: Float): Color {
        return Color.fromRGBA(redRange.lerp(pos1), greenRange.lerp(pos2), blueRange.lerp(pos3), alpha)
    }

    fun expand(arr: FloatArray, index: Int = 0): Color =
        Color.fromRGBA(
            redRange.expand(arr[index]),
            greenRange.expand(arr[index + 1]),
            blueRange.expand(arr[index + 2]),
            alpha
        )
}