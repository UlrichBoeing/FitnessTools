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
    g.ellipse(x, y, radius, radius)
}
