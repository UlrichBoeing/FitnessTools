package de.ulrich_boeing.basics

import kotlin.random.Random

fun Random.floatInRange(from: Float, until: Float) = from + this.nextFloat() * (until - from)

// Replacing Random.nextInt(from: Int, until: Int): Int
// from does not have to be smaller than until
fun Random.intInRange(from: Int, until: Int): Int {
    return when {
        from == until -> from
        from < until -> nextInt(from, until)
        else -> nextInt(until, from)
    }
}
