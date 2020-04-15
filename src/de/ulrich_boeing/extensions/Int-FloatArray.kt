package de.ulrich_boeing.extensions

import de.ulrich_boeing.basics.Vec
import processing.core.PApplet.sin
import kotlin.math.pow
import kotlin.math.roundToInt

fun FloatArray.exp(x: Float) = FloatArray(size) { i -> this[i].pow(x) }
fun FloatArray.sin() = FloatArray(size) { i -> sin(this[i] * Vec.PI) }
fun FloatArray.repeat(times: Int) = FloatArray(size) { i -> (this[i] * times) % 1 }
fun FloatArray.triangle(split: Float): FloatArray = FloatArray(size) { i ->
    if (this[i] < split)
        this[i] / split
    else
        (1 - this[i]) / (1 - split)
}

fun FloatArray.expandToFullRange(): FloatArray {
    val min = this.min() ?: 0f
    val max = this.max() ?: 1f
    return FloatArray(size) { i -> (this[i] - min) / (max - min) }
}

fun FloatArray.expandToInt(min: Int = 0, max: Int = 255): IntArray {
    val range = max - min
    return IntArray(size) { i ->
        (min + range * this[i]).roundToInt()
    }
}


fun IntArray.normalize(min: Int, max: Int): FloatArray {
    val range = max - min
    return FloatArray(size) { i -> (this[i] - min).toFloat() / range }
}