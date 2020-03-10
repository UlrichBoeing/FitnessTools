package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.processing.set
import processing.core.PGraphics

class Line(val points: List<Point>) : Drawable {
    var isCurved = true
    var strokeWeight: Float = 1f

//    fun tweenPoints(count: Int, color: Int = points.first().color): List<Point> {
//
//    }

    override fun draw(g: PGraphics, size: Float) {
        g.set(points.first().color, strokeWeight)
        g.scale(size)

        if (isCurved) {
            points.drawAsCurvedLine(g)
        } else {
            points.drawAsLine(g)
        }
    }
}
