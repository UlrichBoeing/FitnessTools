package de.ulrich_boeing.fitness_tools

import org.tinylog.kotlin.Logger
import java.lang.IllegalArgumentException
import kotlin.random.Random

/**
 *  Holds a value between
 *  start (inclusive) and end (exclusive)
 */
class FloatRange(val start: Float, val end: Float) {
    init {
        require(end >= start) { "end $end is smaller than start $start" }
    }

    /**
     *  Gets a random value between
     *  start (inclusive) and end (exclusive)
     *  */
    fun random(): Float = expand(Random.nextFloat())

    /**
     * Mutate the value.
     * @param range between 0 - 1
     *
     */
    fun mutate(value: Float, range: Float): Float = (1 - range) * value + range * random()


    /**
     *  The inverse function to normalize ✔
     */
    public fun expand(value: Float): Float = start + value * (end - start)

    /**
     *  Normalizing a value ✔
     */
    private fun normalize(value: Float): Float = (value - start) / (end - start)


    /**
     *  Checks if value is inside range ✔
     */
    fun check(value: Float): Float {
        return when {
            value < start -> start
            value > end -> end
            else -> value
        }
    }
}