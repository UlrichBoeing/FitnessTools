package de.ulrich_boeing.basics

import kotlin.math.absoluteValue
import kotlin.random.Random
import java.awt.Color.RGBtoHSB
import java.awt.Color.HSBtoRGB

const val COLOR_WHITE = 0xFFFFFFFF.toInt()
const val COLOR_BLACK = 0xFF000000.toInt()
const val COLOR_RED = 0xFFFF0000.toInt()
const val COLOR_GREEN = 0xFF00FF00.toInt()
const val COLOR_BLUE = 0xFF0000FF.toInt()
const val COLOR_YELLOW = 0xFFFFFF00.toInt()
const val COLOR_AQUA  = 0xFF00FFFF.toInt()
const val COLOR_FUCHSIA = 0xFFFF00FF.toInt()

inline val Int.red: Int
    get() = (this shr 16) and 0xFF

inline val Int.green: Int
    get() = (this shr 8) and 0xFF

inline val Int.blue: Int
    get() = this and 0xFF

inline val Int.alpha: Int
    get() = (this shr 24) and 0xFF

inline val Int.HSB: FloatArray
    get() = RGBtoHSB(this.red, this.green, this.blue, null)

inline fun Int.setAlpha(alpha: Int): Int =
    (this and 0x00FFFFFF) or (alpha shl 24)

inline fun Int.setRed(red: Int): Int =
    Int.fromRGBA(red, this.green, this.blue, this.alpha)

inline fun Int.setGreen(green: Int): Int =
    Int.fromRGBA(this.red, green, this.blue, this.alpha)

inline fun Int.setBlue(blue: Int): Int =
    Int.fromRGBA(this.red, this.green, blue, this.alpha)

inline fun Int.Companion.fromRGBA(red: Int, green: Int, blue: Int, alpha: Int): Int =
    (alpha shl 24) or (red shl 16) or (green shl 8) or blue

inline fun Int.Companion.fromRGB(red: Int, green: Int, blue: Int): Int =
    Int.fromRGBA(red, green, blue, 255)

inline fun Int.Companion.fromHSB(hue: Float, saturation: Float, brightness: Float) =
    HSBtoRGB(hue, saturation, brightness)

fun Int.Companion.randomColor(from: Int = COLOR_BLACK, until: Int = COLOR_WHITE): Int {
    val red = Random.intInRange(from.red, until.red)
    val green = Random.intInRange(from.green, until.green)
    val blue = Random.intInRange(from.blue, until.blue)
    val alpha = Random.intInRange(from.alpha, until.alpha)
    return Int.fromRGBA(red, green, blue, alpha)
}

fun Int.mixColor(other: Int, amount: Float): Int {
    val newRed = lerp(this.red, other.red, amount)
    val newGreen = lerp(this.green, other.green, amount)
    val newBlue = lerp(this.blue, other.blue, amount)
    val newAlpha = lerp(this.alpha, other.alpha, amount)
    return Int.fromRGBA(newRed, newGreen, newBlue, newAlpha)
}

fun Int.getRGBDiff(other: Int): IntArray {
    val redDiff = (this.red - other.red).absoluteValue
    val greenDiff = (this.green - other.green).absoluteValue
    val blueDiff = (this.blue - other.blue).absoluteValue
    return intArrayOf(redDiff, greenDiff, blueDiff)
}

fun Int.getHSBDiff(other: Int): FloatArray {
    val hsb1 = this.HSB
    val hsb2 = other.HSB
    return FloatArray(3) {i -> (hsb1[i] - hsb2[i]).absoluteValue}
}

fun FloatArray.weightedAverage(weight: FloatArray): Float {
    var sum = 0f
    for (i in this.indices) {
        sum += this[i] * weight[i]
    }
    return sum / weight.sum()
}











