package de.ulrich_boeing.fitness_tools

import java.lang.Float.max
import java.lang.Float.min
import kotlin.random.Random

/**
 *  Holds a value between
 *  start (inclusive) and end (exclusive)
 */
class FloatRange(start: Float, end: Float = start) {
    private val constant = (start == end)
    val min = min(start, end)
    val max = max(start, end)

    fun lerp(pos: Float): Float =
        if (constant) min
        else (min + pos * (max - min))

    /**
     *  Gets a random value between
     *  start (inclusive) and end (exclusive)
     *  */
    fun random(): Float =
        if (constant) min
        else expand(Random.nextFloat())

    /**
     * Mutate the value.
     * @param range between 0 - 1
     *
     */
    fun mutate(value: Float, range: Float): Float =
        if (constant) min
        else (1 - range) * value + range * random()


    /**
     *  The inverse function to normalize ✔
     */
    public fun expand(value: Float): Float =
        if (constant) min
        else min + value * (max - min)

    /**
     *  Normalizing a value ✔
     */
    private fun normalize(value: Float): Float =
        if (constant) 0f
        else (value - min) / (max - min)


    /**
     *  Checks if value is inside range ✔
     */
    fun check(value: Float): Float {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatRange

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        return result
    }

    override fun toString(): String {
        return "FloatRange(constant=$constant, min=$min, max=$max)"
    }
}