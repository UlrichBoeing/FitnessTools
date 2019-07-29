package de.ulrich_boeing.fitness_tools

import kotlin.math.roundToInt

class IntRange(val start: Int, val end: Int){
    private val floatRange: FloatRange
    init {
        floatRange = FloatRange(start.toFloat(), (end + 1).toFloat())
    }

    fun random(): Int = convertCheck(floatRange.random())

    fun mutate(value: Int, range: Float): Int {
        // TODO check if range is big enough to change the value
        // TODO check the two versions underneath

//        val newValue = floatRange.mutate(value.toFloat(), range)
//        return check(newValue.roundToInt())

        return convertCheck(floatRange.mutate(value + 0.5f, range))
    }

    private fun convertCheck(value: Float): Int = check(value.toInt())

    fun check(value: Int): Int {
        return when {
            value < start -> start
            value > end -> end
            else -> value
        }
    }

}