package de.ulrich_boeing.basics

import processing.core.PGraphics
import processing.core.PImage
import kotlin.math.absoluteValue
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

fun PImage.toIntArray(): IntArray {
    val arr = IntArray(this.pixels.size * 3)

    this.loadPixels()
    for (i in this.pixels.indices) {
        val color = Color(pixels[i])
        arr[i * 3] = color.red
        arr[i * 3 + 1] = color.green
        arr[i * 3 + 2] = color.blue
    }
    this.updatePixels()

    return arr
}

fun PImage.toByteArray(): ByteArray {
    val arr = ByteArray(this.pixels.size * 3)

    this.loadPixels()
    for (i in this.pixels.indices) {
        val color = Color(pixels[i])
        arr[i * 3] = color.red.toByte()
        arr[i * 3 + 1] = color.green.toByte()
        arr[i * 3 + 2] = color.blue.toByte()
    }
    this.updatePixels()

    return arr
}

fun PImage.getDifference(g: PGraphics, xOffset: Int, yOffset: Int): Long {
    var sum: Long = 0

    g.loadPixels()
    for (y in 0 until g.height) {
        for (x in 0 until g.width) {
            val i1 = (x + xOffset) + (y + yOffset) * this.width
            val i2 = x + y * g.width

            val r1 = (this.pixels[i1] shr 16) and 0xFF
            val g1 = (this.pixels[i1] shr 8) and 0xFF
            val b1 = this.pixels[i1] and 0xFF

            val r2 = (g.pixels[i2] shr 16) and 0xFF
            val g2 = (g.pixels[i2] shr 8) and 0xFF
            val b2 = g.pixels[i2] and 0xFF

            sum += (r1 - r2).absoluteValue + (g1 - g2).absoluteValue + (b1 - b2).absoluteValue
        }
    }
    return sum
}

fun red(color: Int): Int = (color shr 16) and 0xFF
fun green(color: Int): Int = (color shr 8) and 0xFF
fun blue(color: Int): Int = color and 0xFF


fun PImage.sumRGBRect(rect: IntRect): Int {
    var sum: Int = 0
    for (x in rect.x until rect.x + rect.width) {
        for (y in rect.y until rect.y + rect.height) {
            val i = x + y * this.width
            val r = pixels[i] shr 16 and 0xFF  // Faster way of getting red(argb)
            val g = pixels[i] shr 8 and 0xFF   // Faster way of getting green(argb)
            val b = pixels[i] and 0xFF          // Faster way of getting blue(argb)
            sum += r + rgbDifference(g, b)

        }
    }
    return sum
}

fun PImage.sumRGB(): Int {
    var sum: Int = 0

//    this.loadPixels()
    for (i in this.pixels.indices) {
//        val color = Color(pixels[i])
//        sum +=  color.red + color.green + color.blue

        val r = pixels[i] shr 16 and 0xFF  // Faster way of getting red(argb)
        val g = pixels[i] shr 8 and 0xFF   // Faster way of getting green(argb)
        val b = pixels[i] and 0xFF          // Faster way of getting blue(argb)
        sum += r + rgbDifference(g, b)
    }
//    this.updatePixels()

    return sum
}

inline fun rgbDifference(color1: Int, color2: Int): Int {
    return color1 + color2
}