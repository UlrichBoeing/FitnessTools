package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*
import processing.core.PGraphics

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

fun List<Point>.toComparePoints(comparePoint: Point) = List<ComparePoint>(this.size) {i -> ComparePoint(this[i], comparePoint) }

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