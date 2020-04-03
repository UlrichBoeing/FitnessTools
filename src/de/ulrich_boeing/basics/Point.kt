package de.ulrich_boeing.basics

import processing.core.PGraphics

/*
    Kann hier ein Kommentar in deutsch stehen?
    
 */
open class Point(x: Float, y: Float, var color: Int) : Vec(x, y){
    constructor(vec: Vec, color: Int) : this(vec.x, vec.y, color)
    constructor(x: Int, y: Int, color: Int) : this(x.toFloat(), y.toFloat(), color)
}

fun Point.drawAsCircle(g: PGraphics, radius : Float = 16f) {
    g.fill(color)
    g.stroke(COLOR_WHITE.setAlpha(100))
    g.strokeWeight(1f)
    g.ellipse(x, y, radius, radius)
}

class ComparePoint(point: Point, val comparePoint: Point): Point(point.x, point.y, point.color) {
    val distanceTo = point.distance(comparePoint)
    val angleTo = ((point - comparePoint).angle + Vec.TAU) % Vec.TAU
    val rgbDif: Int = (point.color.getRGBDiff(comparePoint.color)).sum()

    init {
        println("angleTo: ${angleTo}")
    }
}
