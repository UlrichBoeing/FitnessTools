package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.canvas.CanvasLayer
import processing.core.PGraphics
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

fun List<Vec>.drawAsShape(g: PGraphics) {
    g.beginShape()
    for (p in this) {
        g.vertex(p.x, p.y)
    }
    g.endShape()
}

fun List<Vec>.drawAsLine(g: PGraphics) {
    g.beginShape()
    for (p in this) {
        g.vertex(p.x, p.y)
    }
    g.endShape(PGraphics.CLOSE)
}

fun List<Vec>.drawAsCurvedShape(g: PGraphics) {
    g.beginShape()
    // draw from point 1, point 0 is control point
    for (p in this) {
        g.curveVertex(p.x, p.y)
    }
    // draw until point 1, point 2 is control point
    for (p in this.subList(0, 3)) {
        g.curveVertex(p.x, p.y)
    }
    println("Breite von g: " + g.width)
    g.endShape()
}

fun List<Vec>.drawAsCircles(g: PGraphics) {
    for (p in this) {
        p.drawAsCircle(g)
    }
}

fun List<Vec>.drawAsCurvedLine(g: PGraphics) {
    g.beginShape()
    g.curveVertex(this.first().x, this.first().y)
    for (p in this) {
        g.curveVertex(p.x, p.y)
    }
    g.curveVertex(this.last().x, this.last().y)
    g.endShape()
}

fun List<Vec>.drawPointNumbers(g: PGraphics, textColor: Int = COLOR_RED) {
    g.noStroke()
    g.fill(textColor)
    for (i in this.indices) {
        g.text(i.toString(), this[i].x, this[i].y)
    }
}

fun List<Vec>.toListOfPoints(color: Int): List<Point> {
    val list = mutableListOf<Point>()
    for (vec in this) {
        list.add(Point(vec, color))
    }
    return list
}

fun createPolygon(count: Int) = List(count) { i ->
    Vec(cos(i * Vec.TAU / count), sin(i * Vec.TAU / count))
}

fun createLine(start: Vec, end: Vec, gap: Float): List<Vec> {
    val distance = start.distance(end)
    val count = (distance / gap).roundToInt()
    return start.lerpList(end, count)
}

fun createGridFromCenter(center:Vec, width: Float, height: Float, gap: Float): List<Vec> {
    val rect = Rect.fromCenter(center, width, height)
    return createGrid(rect, gap)
}

fun createGrid(rect: Rect, gap: Float): List<Vec> {
    val vecList = mutableListOf<Vec>()
    val xCount = (rect.width / gap).roundToInt()
    val yCount = (rect.height / gap).roundToInt()

    for (x in 0..xCount)
        for (y in 0..yCount)
            vecList.add(Vec(rect.x + (rect.width / xCount) * x, rect.y + (rect.height / yCount) * y))

    return vecList
}

fun List<Vec>.toPoints(canvas: CanvasLayer): List<Point> = canvas.getColors(this)

fun List<Vec>.center(range: IntRange = 0 until size): Vec {
    val sum = range.fold(Vec(0, 0)) { sum, element -> sum + this[element % size] }
    return sum.div(range.count().toFloat())
}

fun List<Vec>.tweenPointsOfShape():List<Vec> = List<Vec>(size) {i -> this.center(i ..(i + 1))}

fun List<Vec>.zipTweenPoints(tweenPoints: List<Vec>): List<Vec> {
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

operator fun List<Vec>.plus(vec: Vec): List<Vec> {
    return List(this.size) { i -> this[i] + vec }
}

fun List<Vec>.plusToList(vec: Vec): List<Vec> {
    for (vecInList in this)
         vecInList += vec
    return this
}
operator fun List<Vec>.times(n: Float): List<Vec> {
    return List(this.size) {i -> this[i].times(n)}
}


fun List<Vec>.shiftInCircle(radius: Float) = List(this.size) { i -> this[i].shiftInCircle(radius) }

fun List<Vec>.move(vec: Vec) = List(this.size) { i -> vec + this[i] }
fun List<Vec>.resize(n: Float) = List(this.size) { i -> this[i] * n }
fun List<Vec>.resize(min: Float, max: Float) = List(this.size) { i -> this[i] * Random.nextFloatInRange(min, max) }

fun Random.nextFloatInRange(min: Float, max: Float) = min + this.nextFloat() * (max - min)

