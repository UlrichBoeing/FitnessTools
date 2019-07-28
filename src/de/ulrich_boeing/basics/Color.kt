package de.ulrich_boeing.basics

import kotlin.math.absoluteValue
import kotlin.random.Random

inline class Color(val rgba: Int) {
    companion object {
        val white = fromRGBA(255, 255, 255, 255)
        val black = fromRGBA(0, 0, 0, 255)
        val red = fromRGBA(255, 0, 0, 255)
        val blue = fromRGBA(0, 0, 255, 255)
        inline fun fromRGBA(red: Int, green: Int, blue: Int, alpha: Int): Color =
            Color((alpha shl 24) or (red shl 16) or (green shl 8) or blue)
        fun fromRandom(alpha: Int) =
            fromRGBA(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255), alpha)
        fun randomInRange(from: Color, until: Color): Color {
            val red = Random.intInRange(from.red, until.red)
            val green = Random.intInRange(from.green, until.green)
            val blue = Random.intInRange(from.blue, until.blue)
            val alpha = Random.intInRange(from.alpha, until.alpha)
            return fromRGBA(red, green, blue, alpha)
        }
    }

    inline val red: Int
        get() = ((rgba shr 16) and 0xFF)
    inline val green: Int
        get() = ((rgba shr 8) and 0xFF)
    inline val blue: Int
        get() = (rgba and 0xFF)
    inline val alpha: Int
        get() = ((rgba shr 24) and 0xFF)

    inline fun setAlpha(alpha: Int): Color = fromRGBA(red, green, blue, alpha)
    inline fun setRed(red: Int): Color = fromRGBA(red, green, blue, alpha)
    inline fun setGreen(green: Int): Color = fromRGBA(red, green, blue, alpha)
    inline fun setBlue(blue: Int): Color = fromRGBA(red, green, blue, alpha)

    inline fun getDiff(other: Color): Int {
        val redDiff = (red - other.red).absoluteValue
        val greenDiff = (green - other.green).absoluteValue
        val blueDiff = (blue - other.blue).absoluteValue
        return redDiff + greenDiff + blueDiff
    }
}