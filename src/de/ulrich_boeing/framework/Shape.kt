package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.*
import de.ulrich_boeing.processing.set
import processing.core.PGraphics

class Shape(val points: List<Point>, override var group: Group? = null) : Drawable {
    var isCurved = true
    var strokeWeight: Float = 0f

    override fun draw(g: PGraphics) {
        g.set(points.first().color, strokeWeight)

        if (isCurved) {
            points.drawAsCurvedShape(g)
        } else {
            points.drawAsShape(g)
        }
    }
}