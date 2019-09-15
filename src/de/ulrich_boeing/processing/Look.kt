package de.ulrich_boeing.processing

import de.ulrich_boeing.basics.Clipping
import de.ulrich_boeing.basics.Color
import processing.core.PGraphics

class Look(val fill: Color, val stroke: Color, val strokeWeight: Float = 1f) {
    companion object {
        val red = Look(
            Color.fromRGBA(255, 0, 0, 255),
            Color.fromRGBA(0, 0, 0, 0), 0f)
        val green = Look(
            Color.fromRGBA(0, 255, 0, 255),
            Color.fromRGBA(0, 0, 0, 0), 0f)
        val blue = Look(
            Color.fromRGBA(0, 0, 255, 255),
            Color.fromRGBA(0, 0, 0, 0), 0f)
        val redOutline = Look(
            Color.fromRGBA(255, 255, 255, 96),
            Color.fromRGBA(255, 0, 0, 128))
        val greenOutline = Look(
            Color.fromRGBA(255, 255, 255, 96),
            Color.fromRGBA(0, 255, 0, 128))
        val blueOutline = Look(
            Color.fromRGBA(255, 255, 255, 96),
            Color.fromRGBA(0, 0, 255, 128))
        val solidGreenOutline = Look(
            Color.fromRGBA(0, 255, 0, 128),
            Color.fromRGBA(255, 255, 255, 128))
        val transparentRed = Look(
            Color.fromRGBA(255, 0, 0, 30),
            Color.fromRGBA(0, 0, 0, 0), 0f)


    }
    fun set(g: PGraphics) {
        if (fill.alpha > 0)
            g.fill(fill.rgba)
        else
            g.noFill()

        if (stroke.alpha > 0 && strokeWeight > 0) {
            g.strokeWeight(strokeWeight)
            g.stroke(stroke.rgba)
        }
        else
            g.noStroke()
    }

}