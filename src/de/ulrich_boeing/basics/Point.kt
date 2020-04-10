package de.ulrich_boeing.basics

import processing.core.PGraphics
import kotlin.math.absoluteValue

/*
    Kann hier ein Kommentar in deutsch stehen?
    
 */
open class Point(x: Float, y: Float, var color: Int) : Vec(x, y){
    constructor(vec: Vec, color: Int) : this(vec.x, vec.y, color)
    constructor(x: Int, y: Int, color: Int) : this(x.toFloat(), y.toFloat(), color)
}

fun Point.drawAsCircle(g: PGraphics, radius : Float = 16f) {
    g.fill(color)
    g.noStroke()
//    g.stroke(COLOR_WHITE.setAlpha(100))
    g.strokeWeight(1f)
    g.ellipse(x, y, radius, radius)
}

class ComparePoint(point: Point, val comparePoint: Point): Point(point.x, point.y, point.color) {
    val distanceTo = point.distance(comparePoint)
    val angleTo = ((point - comparePoint).angle + Vec.TAU) % Vec.TAU
    val rgbDif: Int = (point.color.getRGBDiff(comparePoint.color)).sum()
    val HSB = point.color.HSB
    val brightDif : Float = (HSB[0] - comparePoint.color.HSB[0]).absoluteValue

    init {
//        println("angleTo: ${angleTo}")
    }
}
