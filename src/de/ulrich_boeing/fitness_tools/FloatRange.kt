package de.ulrich_boeing.fitness_tools

import org.tinylog.kotlin.Logger
import java.lang.IllegalArgumentException
import kotlin.random.Random

/**
 * End value can be reached (although not returned by random(), but through mutation)
 */
class FloatRange(val start: Float, val end: Float) {
    init {
        if (end < start)
            throw IllegalArgumentException("end $end is smaller than start $start")

    }

    /**
     *  Gets a random value between
     *  start (inclusive) and end (exclusive) ✔
     */
    fun random(): Float = expand(Random.nextFloat())

    /**
     * Mutate the value. ✔
     * @param range between 0% - 100% of (end - start) in each direction
     *
     */
    fun mutate(value: Float, range: Float): Float {
        val mutation = mutation(range, Random.nextFloat() * 2 - 1)
        val newValue = value + mutation
        val checkedNewValue = check(newValue)

//        Logger.trace("oldValue = {0.00} [start={0.00} < end={0.00}]", value, start, end)
//        Logger.trace("range = {0.00}%", range)
//        Logger.trace("newValue = {0.00}  [from {0.00} to {0.00}]", newValue,
//            value + mutation(range, -1f), value + mutation(range, 1f))
//        if (checkedNewValue != newValue)
//            Logger.trace("newValue changed = {0.00}", checkedNewValue)

        return checkedNewValue
    }

    /**
     * @range: mutationRange in %
     * @param pos between -1 and 1
     *
     */
    private fun mutation(range: Float, pos: Float): Float {
        val valueRange = (range / 100f) * (end - start)
        return valueRange  * pos
    }

    /**
     *  The inverse function to normalize ✔
     */
    private fun expand(value: Float): Float = start + value * (end - start)

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