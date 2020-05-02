package de.ulrich_boeing.processing

import processing.core.PApplet
import processing.core.PGraphics

fun PGraphics.set(color: Int, strokeWeight: Float = 0f) {
    if (strokeWeight == 0f) {
        this.fill(color)
        this.noStroke()
    } else {
        this.stroke(color)
        this.strokeWeight(strokeWeight)
        this.noFill()
    }
}

fun PApplet.fillOnly(color: Int) {
    fill(color)
    noStroke()
}
fun PApplet.strokeOnly(color: Int, weight : Float = 0f) {
    noFill()
    stroke(color)
    if (weight > 0)
        strokeWeight(weight)
}