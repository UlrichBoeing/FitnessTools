package de.ulrich_boeing.framework

import de.ulrich_boeing.basics.COLOR_RED
import de.ulrich_boeing.basics.Point
import de.ulrich_boeing.basics.Vec
import processing.core.PGraphics

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

