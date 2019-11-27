package de.ulrich_boeing.basics

import kotlin.math.cos
import kotlin.math.sin

class Circle(val center: Vec, val radius: Float) {
    constructor() : this(Vec(0, 0), 1f)
    constructor(radius: Float) : this(Vec(0, 0), radius)
    constructor(x: Int, y: Int, radius: Float) : this(Vec(x, y), radius)

    fun edgePoints(count: Int): List<Vec> {
        val list = mutableListOf<Vec>()
        for (i in 0 until count) {
            val angle = (Vec.TAU * i) / count
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)
            list.add(Vec(x, y))
        }
        return list
    }
}