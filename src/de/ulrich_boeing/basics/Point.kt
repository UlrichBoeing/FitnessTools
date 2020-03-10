package de.ulrich_boeing.basics

/*
    Kann hier ein Kommentar in deutsch stehen?
    
 */
class Point(x: Float, y: Float, var color: Int) : Vec(x, y){
    constructor(vec: Vec, color: Int) : this(vec.x, vec.y, color)
    constructor(x: Int, y: Int, color: Int) : this(x.toFloat(), y.toFloat(), color)
}