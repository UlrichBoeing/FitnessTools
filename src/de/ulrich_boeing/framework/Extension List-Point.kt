package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*
import processing.core.PGraphics
import kotlin.math.pow
import kotlin.random.Random

fun List<Point>.drawAsCircles(g: PGraphics, radius: Float = 16f) {
    for (p in this) {
        p.drawAsCircle(g, radius)
    }
}

fun List<ComparePoint>.averageRGBDif(): Float {
    val sum = this.fold(0) { sum, point -> sum + point.rgbDif }
    return sum / this.size.toFloat()
}

/**
 * return index of first element in the list with a RGB-difference larger than limit
 */
fun List<Point>.indexOfFirstDif(color: Int, limit: Int): Int {
    var index = 0
    while (index < this.size) {
        val listColor = this[index].color
        if (listColor.getRGBDiff(color).sum() > limit)
            return index
        index++
    }
    print(this.lastIndex)
    return this.lastIndex
}

fun List<Point>.meanColor(): Int {
    if (this.isEmpty())
        return Int.Companion.fromRGBA(0, 0, 0, 1)

    var sumRed = 0
    var sumGreen = 0
    var sumBlue = 0
    var sumAlpha = 0

    for (point in this) {
        sumRed += point.color.red
        sumGreen += point.color.green
        sumBlue += point.color.blue
        sumAlpha += point.color.alpha
    }
    return Int.fromRGBA(sumRed / this.size, sumGreen / this.size, sumBlue / this.size, sumAlpha / this.size)
}

fun List<Point>.distanceWeightedSumAlpha(center: Vec, maxDistance: Float): Float {
    if (this.isEmpty())
        return 0f

    var sumAlpha = 0f
    val weights = this.distanceWeight(center, maxDistance)
    for (i in this.indices) {
        sumAlpha += this[i].color.alpha * weights[i]
    }
    return sumAlpha
}

/**
 * Farb-Mittelwert, die Farbe jedes Punktes ist gewichtet nach der Entfernung zu einem center-Vector
 */
fun List<Point>.distanceWeightedMeanColor(center: Vec, maxDistance: Float): Int {
    if (this.isEmpty())
        return Int.Companion.fromRGBA(0, 0, 0, 1)

    var sumRed = 0f
    var sumGreen = 0f
    var sumBlue = 0f
    var sumAlpha = 0f
    val weights = this.distanceWeight(center, maxDistance)
    val sumWeights = weights.sum()

    for (i in this.indices) {
        sumRed += this[i].color.red * weights[i]
        sumGreen += this[i].color.green * weights[i]
        sumBlue += this[i].color.blue * weights[i]
        sumAlpha += this[i].color.alpha * weights[i]
//        println("alpha: ${this[i].color.alpha}, weight = ${weights[i]}")
    }
//    println("sumAlpha: $sumAlpha, sumWeights: $sumWeights")
    var alpha = 255f
//    alpha = if (alpha > 40) alpha else Random.nextFloat() * 40
//    alpha = alpha.coerceIn(1f, 254f)
    return Int.fromRGBA(sumRed / sumWeights, sumGreen / sumWeights, sumBlue / sumWeights, alpha)
}

fun List<Point>.distanceWeight(center: Vec, maxDistance: Float): FloatArray {
    val arr = FloatArray(this.size) { 0f }
    for (i in this.indices) {
        val weight = (maxDistance - center.distance(this[i])) / maxDistance
        arr[i] = if (weight > 0f) weight else 0f
    }
    return arr
}

fun List<Point>.toComparePoints(comparePoint: Point) =
    List<ComparePoint>(this.size) { i -> ComparePoint(this[i], comparePoint) }

//fun List<ComparePoint>.toPoints(): List<Point> = List<Point>(this.size) {i -> this[i].point}
/**
 * return first element in the list with a RGB-difference larger than limit
 */
fun List<Point>.firstDif(color: Int, limit: Int): Point = this[indexOfFirstDif(color, limit)]

fun List<Point>.cutByColorDif(limit: Int): List<Point> {
    var i = 1
    while (this[i - 1].color.getRGBDiff(this[i].color).sum() < limit) {
        i++
        if (i == this.size)
            break
    }
    return this.subList(0, i)
}

fun List<Point>.colorDifToNext(): IntArray = IntArray(this.size - 1) { i ->
    this[i].color.getRGBDiff(this[i + 1].color).sum()
}

fun List<Point>.setColor(color: Int): List<Point> {
    for (p in this) {
        p.color = color
    }
    return this
}

fun List<Point>.setColor(color1: Int, color2: Int) {
    for (i in 0..this.lastIndex) {
        this[i].color = color1.mixColor(color2, i.toFloat() / this.lastIndex)
    }
}

fun IntArray.indexOfMax(): Int {
    if (this.size == 0)
        throw RuntimeException("unable to find indexOfMax in empty IntArray")

    var max = Integer.MIN_VALUE
    var maxIndex = 0
    for (i in this.indices) {
        if (this[i] > max) {
            max = this[i]
            maxIndex = i
        }
    }
    return maxIndex
}