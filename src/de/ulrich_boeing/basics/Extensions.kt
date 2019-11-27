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

// g must have the same dimensions as image
fun PImage.getDifference(g: PGraphics, clippings: Array<Clipping>): Long {
    g.loadPixels()
    var sum = 0L
    for (clipping in clippings) {
        sum += this.getDifference(g, clipping)
    }
    return sum
}

// g must have the same dimensions as image
fun PImage.getDifference(g: PGraphics, clipping: Clipping): Long {
    val imageClipping = Clipping(0, 0, this.width, this.height)
    val intersection = imageClipping.intersect(clipping)

    val xRange = intersection.left until intersection.right
    val yRange = intersection.top until intersection.bottom

    var sum = 0L
    for (y in yRange) {
        for (x in xRange) {
            val i = x + y * this.width

            val r1 = (this.pixels[i] shr 16) and 0xFF
            val g1 = (this.pixels[i] shr 8) and 0xFF
            val b1 = this.pixels[i] and 0xFF


            val r2 = (g.pixels[i] shr 16) and 0xFF
            val g2 = (g.pixels[i] shr 8) and 0xFF
            val b2 = g.pixels[i] and 0xFF

            sum += (r1 - r2).absoluteValue + (g1 - g2).absoluteValue + (b1 - b2).absoluteValue
        }
        g.updatePixels()
    }
    return sum

}

// g must have the same dimensions as image
fun PImage.getDifference(g: PGraphics): Float {
    g.loadPixels()
    var sum = 0L
    var count = 0L
    for (i in g.pixels.indices) {
        val gAlpha = (g.pixels[i] shr 24) and 0xFF
        if (gAlpha != 0) {
            val r1 = (this.pixels[i] shr 16) and 0xFF
            val g1 = (this.pixels[i] shr 8) and 0xFF
            val b1 = this.pixels[i] and 0xFF


            val r2 = (g.pixels[i] shr 16) and 0xFF
            val g2 = (g.pixels[i] shr 8) and 0xFF
            val b2 = g.pixels[i] and 0xFF
            sum += (r1 - r2).absoluteValue + (g1 - g2).absoluteValue + (b1 - b2).absoluteValue
            count++
        }
    }
    g.updatePixels()
    return sum.toFloat() / count
}

fun PImage.getDifference(g: PGraphics, xOffset: Int, yOffset: Int): Long {
    val imageClipping = Clipping(0, 0, this.width, this.height)
    val gClipping = Clipping(xOffset, yOffset, g.width, g.height)
    val intersection = imageClipping.intersect(gClipping)

    val xRange = intersection.left until intersection.right
    val yRange = intersection.top until intersection.bottom

    g.loadPixels()
    var sum = 0L
    for (y in yRange) {
        for (x in xRange) {
            val i1 = x + y * this.width
            val i2 = (x - intersection.x) + ((y - intersection.y) * g.width)

            val r1 = (this.pixels[i1] shr 16) and 0xFF
            val g1 = (this.pixels[i1] shr 8) and 0xFF
            val b1 = this.pixels[i1] and 0xFF


            val r2 = (g.pixels[i2] shr 16) and 0xFF
            val g2 = (g.pixels[i2] shr 8) and 0xFF
            val b2 = g.pixels[i2] and 0xFF

            sum += (r1 - r2).absoluteValue + (g1 - g2).absoluteValue + (b1 - b2).absoluteValue
        }
        g.updatePixels()
    }
    return sum
}

// Performing a weighted choice, the higher the value the more likely it is to be picked
fun FloatArray.selectOne(): Int {
    var r = Random.nextFloat() * this.sum()

    var index = -1
    do {
        r -= this[++index]
    } while (r > 0 && index < (size -1))
    return index
}

fun FloatArray.indexOfHighest(count : Int): IntArray {
    val highest = IntArray(count)
    val cache = FloatArray(count)
    for (i in highest.indices) {
        highest[i] = this.indexOfMax()
        cache[i] = this[highest[i]]
        this[highest[i]] = Float.MIN_VALUE
    }
    for (i in highest.indices)
        this[highest[i]] = cache[i]

    return highest
}

fun FloatArray.indexOfMax(): Int =
    this.indices.maxBy { this[it] } ?: throw RuntimeException("index of max value could not be found")


fun FloatArray.indexOfMin(): Int =
    this.indices.minBy { this[it] } ?: throw RuntimeException("index of min value could not be found")

fun FloatArray.setSum(sum: Float): FloatArray {
    val factor = sum / this.sum()
    return FloatArray(this.size) { i -> this[i] * factor}
}

fun FloatArray.norm() {
    val min = this.min() ?: 0f
    val max = this.max() ?: 1f
    var range = max - min
    if (range < 0.00001f)
        range = 0.00001f

    for (i in this.indices) {
        this[i] = ((this[i] - min) / (range))
    }
}

fun FloatArray.mix(other: FloatArray, split: Int): FloatArray =
    FloatArray(size) { i -> if (i < split) this[i] else other[i] }


fun red(color: Int): Int = (color shr 16) and 0xFF
fun green(color: Int): Int = (color shr 8) and 0xFF
fun blue(color: Int): Int = color and 0xFF

