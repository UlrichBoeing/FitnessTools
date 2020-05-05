package de.ulrich_boeing.basics

import processing.core.PApplet.abs
import processing.core.PGraphics
import kotlin.math.cos
import kotlin.math.sin

class Circle(val center: Vec, val radius: Float) {
    constructor() : this(Vec(0, 0), 1f)
    constructor(radius: Float) : this(Vec(0, 0), radius)
    constructor(x: Int, y: Int, radius: Float) : this(Vec(x, y), radius)

    /**
     * Punkte auf dem Rand des Kreises
     */
    fun edgePoints(count: Int): List<Vec> {
        val list = mutableListOf<Vec>()
        for (i in 0 until count) {
            val angle = (Vec.TAU * i) / count
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)
            list.add(Vec(x, y))
        }
        return list
    }

    fun contains(vec: Vec): Boolean = vec.squareDistance(center) <= radius.square()

    infix fun intersects(rect: Rect): Boolean {
        val distanceToRect = Vec(abs(center.x - rect.center.x), abs(center.y - rect.center.y))

        return when {
            distanceToRect.x > (radius + rect.width / 2) -> false
            distanceToRect.y > (radius + rect.height / 2) -> false
            distanceToRect.x <= rect.width / 2 -> true
            distanceToRect.y <= rect.height / 2 -> true
            else -> {
                val cornerDistanceSquared = (distanceToRect.x - rect.width / 2).square() + (distanceToRect.y - rect.height / 2).square()
                cornerDistanceSquared <= radius.square()
            }
        }
    }
}

fun Circle.draw(g: PGraphics) {
    g.ellipse(center.x, center.y, radius * 2, radius * 2)
}
