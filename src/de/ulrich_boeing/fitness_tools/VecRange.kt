package de.ulrich_boeing.fitness_tools

import de.ulrich_boeing.basics.Vec

class VecRange(val start: Vec, val end: Vec) {
    constructor(end: Vec): this(Vec(0, 0), end)

    val xRange = FloatRange(start.x, end.x)
    val yRange = FloatRange(start.y, end.y)

    fun random(): Vec = Vec(xRange.random(), yRange.random())

    fun mutate(vec: Vec, range: Float) {
        vec.x = xRange.mutate(vec.x, range)
        vec.y = yRange.mutate(vec.y, range)
    }





}

