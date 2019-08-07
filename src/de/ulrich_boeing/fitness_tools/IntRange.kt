package de.ulrich_boeing.fitness_tools

import java.lang.IllegalArgumentException
import kotlin.math.floor

class IntRange(val start: Int, val end: Int){
    private val floatRange: FloatRange
    init {
        if (end < start)
            throw IllegalArgumentException("end $end is smaller than start $start")

        floatRange = FloatRange(start.toFloat(), (end + 1).toFloat())
    }

    fun random(): Int = convertCheck(floatRange.random())

    fun mutate(value: Int, range: Float): Int {
        return convertCheck(floatRange.mutate(value + 0.5f, range))
    }

    private fun convertCheck(value: Float): Int = check(floor(value).toInt())

    fun check(value: Int): Int {
        return when {
            value < start -> start
            value > end -> end
            else -> value
        }
    }

}