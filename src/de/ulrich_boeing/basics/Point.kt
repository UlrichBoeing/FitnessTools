package de.ulrich_boeing.basics

import processing.core.PGraphics

/*
    Kann hier ein Kommentar in deutsch stehen?
    
 */
class Point(x: Float, y: Float, var color: Int) : Vec(x, y){
    constructor(vec: Vec, color: Int) : this(vec.x, vec.y, color)
    constructor(x: Int, y: Int, color: Int) : this(x.toFloat(), y.toFloat(), color)
}

fun Point.drawAsCircle(g: PGraphics, radius : Float = 16f) {
    g.fill(color)
    g.stroke(COLOR_WHITE.setAlpha(100))
    g.strokeWeight(1f)
    g.ellipse(x, y, radius, radius)
}

class ComparePoint(val point: Point, val comparePoint: Point) {
    val distance = point.distance(comparePoint)
    val angle = (point - comparePoint).angle + Vec.PI
    val rgbDif: Int = (point.color.getRGBDiff(comparePoint.color)).sum()

    init {
        println("angle: ${angle.toDegrees()}")
    }
}
