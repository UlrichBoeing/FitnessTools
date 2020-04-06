package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.ComparePoint
import de.ulrich_boeing.basics.Vec

fun List<ComparePoint>.splitAngles(countSlices: Int): List<List<ComparePoint>> {
    var slices = List(countSlices) { mutableListOf<ComparePoint>() }

    for (cPoint in this) {
        val index = (cPoint.angleTo / Vec.TAU * countSlices).toInt()
        slices[index].add(cPoint)
    }
    return slices
}

fun List<List<ComparePoint>>.removeEmpty(): List<List<ComparePoint>> {
    val newList = mutableListOf<List<ComparePoint>>()
    for (list in this) {
        if (list.isNotEmpty())
            newList.add(list)
    }
    return newList
}

fun List<ComparePoint>.indexOfFirstDif(limit: Int): Int {
    if (isEmpty())
        println("list is empty!!!")
    val maxDif = this.maxBy { it.rgbDif }
    var newLimit = limit
    if (maxDif!!.rgbDif < 100)
        newLimit = maxDif!!.rgbDif / 2

    var i = 0
    while (i < this.size) {
        if (this[i].rgbDif > newLimit)
            return i
        i++
    }
    return this.lastIndex
}

fun List<ComparePoint>.zipTweenPoints(tweenPoints: List<ComparePoint>): List<ComparePoint> {
    val newSize = when {
        size == tweenPoints.size -> 2 * size
        size  - 1 == tweenPoints.size -> 2 * size -1
        else -> throw RuntimeException("zip not possible, list tweenPoints has wrong size.")
    }
    var newList = MutableList(newSize) { i -> this[i / 2] }
    for (i in tweenPoints.indices) {
        newList[i * 2 + 1] = tweenPoints[i]
    }
    return newList
}

