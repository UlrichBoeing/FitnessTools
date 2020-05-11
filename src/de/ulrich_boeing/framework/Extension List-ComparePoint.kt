package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*

fun List<ComparePoint>.splitAngles(countSlices: Int): List<List<ComparePoint>> {
    var slices = List(countSlices) { mutableListOf<ComparePoint>() }

    for (cPoint in this) {
        val index = (cPoint.angleTo / Vec.TAU * countSlices).toInt()
        slices[index].add(cPoint)
    }
    return slices
}

fun List<ComparePoint>.getNearby(other: ComparePoint, distance: Float): List<ComparePoint> {
    val newList = mutableListOf<ComparePoint>()
    val squareDistance = distance * distance
    for (p in this) {
        if (p != other)
            if (p.squareDistance(other) < squareDistance)
                newList.add(p)
    }
    return newList
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
        newLimit = maxDif.rgbDif / 2

    var i = 0
    while (i < this.size) {
        if (this[i].rgbDif > newLimit)
            return i
        i++
    }
    return this.lastIndex
}

fun List<ComparePoint>.indexOfFirstBrightDif(limit: Float): Int {
    if (isEmpty())
        println("list is empty!!!")
    val maxDif = this.maxBy { it.brightDif }
    var newLimit = limit
    if (maxDif!!.brightDif < 0.4)
        newLimit = maxDif.brightDif / 2

    var i = 0
    while (i < this.size) {
        if (this[i].brightDif > newLimit)
            return i
        i++
    }
    return this.lastIndex
}

fun List<ComparePoint>.getTweenPointOfShape(index: Int, pool: List<ComparePoint>): ComparePoint {
    val nextIndex = if (index < this.lastIndex) index + 1 else 0
    val middlePoint = (this[index] + this[nextIndex]) * 0.5f
    val fullRadius = (this[index] - middlePoint).length
    val aroundMiddlePoint = pool.filter { it.distance(middlePoint) < fullRadius * 0.5f }
    val i = aroundMiddlePoint.indexOfFirstDif(100)
    val insertPoint = aroundMiddlePoint[i]

    insertPoint.color = COLOR_RED
    return insertPoint
}

