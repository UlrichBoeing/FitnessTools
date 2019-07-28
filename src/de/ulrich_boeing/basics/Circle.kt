package de.ulrich_boeing.basics

import de.ulrich_boeing.fitness_tools.ColorRange
import de.ulrich_boeing.fitness_tools.FloatRange
import de.ulrich_boeing.fitness_tools.VecRange
import kotlin.random.Random

class Circle(var center: Vec, var radius: Float, var color: Color) {
    companion object {
        val vecRange = VecRange(Vec(800, 800))
        val radiusRange = FloatRange(12f, 80f)
        val colorRange = ColorRange(Color.black , Color.white)


        fun getRandom(): Circle = Circle(vecRange.random(), radiusRange.random(), colorRange.random())
    }

    var strokeWeight = 0f

    fun mutate() {
        vecRange.mutate(center, 0.003f)
        radius = radiusRange.mutate(radius, 0.08f)
        color = colorRange.mutate(color, 0.05f)
//        if (radius == radiusRange.end)
//            println("top is reached")

    }
}
