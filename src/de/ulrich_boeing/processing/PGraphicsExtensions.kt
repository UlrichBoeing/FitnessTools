package de.ulrich_boeing.processing

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