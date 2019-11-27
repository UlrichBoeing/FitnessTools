package de.ulrich_boeing.fitness_tools

import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.math.min
import kotlin.math.max

class IntegerRange(val start: Int, val end: Int = start) {
    val min = min(start, end)
    val max = max(start, end)

    fun random(): Int = Random.nextInt(min, max)

    fun lerp(pos: Float): Int {
        return (min + pos * (max - min)).roundToInt()
    }

    fun mutate(value: Int, range: Float): Int {
        val value = (1 - range) * value + range * random()
        return check(value.roundToInt())
    }

    fun expand(value: Float): Int = start + (value * (end - start)).roundToInt()


    fun check(value: Int): Int {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }

}